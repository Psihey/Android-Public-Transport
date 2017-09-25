package com.provectus.public_transport.fragment.mapfragment.impl;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.orhanobut.logger.Logger;
import com.provectus.public_transport.R;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.model.DirectEntity;
import com.provectus.public_transport.model.StopEntity;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.VehiclesModel;
import com.provectus.public_transport.model.converter.TransportType;
import com.provectus.public_transport.persistence.database.DatabaseHelper;
import com.provectus.public_transport.service.retrofit.RetrofitProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MapsFragmentPresenterImpl implements MapsFragmentPresenter {
    private static final int TRAM_NUMBER_INCREMENT = 1000;
    private static final int TROLLEY_NUMBER_INCREMENT = 100;

    private MapsFragment mMapsFragment;
    private boolean mIsSelectRoute;
    private int mTransportNumber;
    private long mCurrentRouteServerId;
    private CompositeDisposable mCompositeDisposable;
    private List<Long> mCurrentVehicles = new ArrayList<>();

    @Override
    public void bindView(MapsFragment mapsFragment) {
        mMapsFragment = mapsFragment;
        Logger.d("Maps is binded to its presenter.");
        EventBus.getDefault().register(this);
    }

    @Override
    public void unbindView() {
        mMapsFragment = null;
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        Logger.d("Maps is unbind from presenter");
    }

    @Override
    public void unregisteredEventBus() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BusEvents.SendChosenRouter event) {
        mIsSelectRoute = event.getSelectRout().isSelected();
        String transportType = event.getSelectRout().getType().toString();
        if (transportType.equals(TransportType.TROLLEYBUSES_TYPE.name())) {
            mTransportNumber = event.getSelectRout().getNumber() + TROLLEY_NUMBER_INCREMENT;
        } else if (transportType.equals(TransportType.TRAM_TYPE.name())) {
            mTransportNumber = event.getSelectRout().getNumber() + TRAM_NUMBER_INCREMENT;
        }
        mMapsFragment.getInfoTransport(mTransportNumber, mIsSelectRoute);

        if (mMapsFragment.checkOnReadyMap()) {
            DatabaseHelper.getPublicTransportDatabase().transportDao().getTransportEntity(event.getSelectRout().getNumber(), transportType)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Logger.d(throwable.getMessage()))
                    .subscribe(this::getTransportFromDB);
            DatabaseHelper.getPublicTransportDatabase().transportDao().getStopsForCurrentTransport(event.getSelectRout().getNumber(), transportType)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Logger.d(throwable.getMessage()))
                    .subscribe(this::getStopsFromDB);
            DatabaseHelper.getPublicTransportDatabase().transportDao().getDirectionEntity(event.getSelectRout().getNumber(), transportType)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Logger.d(throwable.getMessage()))
                    .subscribe(this::getDirectionFromDB);

        }else {
            mMapsFragment.showErrorSnackbar(R.string.snack_bar_map_not_ready);
        }
    }

    private void getDirectionFromDB(List<DirectEntity> segmentEntities) {
        PolylineOptions polylineOptions = new PolylineOptions();
        for (DirectEntity directionEntity : segmentEntities) {
            polylineOptions.add(new LatLng(directionEntity.getLatitude(), directionEntity.getLongitude()));
        }
        mMapsFragment.drawRoutesWithDirection(polylineOptions);
    }

    private void getStopsFromDB(List<StopEntity> stopEntities) {
        if (mIsSelectRoute && stopEntities.isEmpty()) {
            mMapsFragment.showErrorSnackbar(R.string.snack_bar_no_stops_for_this_route);
        }
        List<MarkerOptions> markerOption = new ArrayList<>();
        for (int i = 0; i < stopEntities.size(); i++) {
            double lat = stopEntities.get(i).getLatitude();
            double lng = stopEntities.get(i).getLongitude();
            markerOption.add(new MarkerOptions().position(new LatLng(lat, lng)));
        }

        mMapsFragment.drawStops(markerOption);
    }

    private void getTransportFromDB(TransportEntity transportEntity) {
        mCurrentRouteServerId = transportEntity.getServerId();
        getVehiclesPosition();
    }

    private void getVehiclesPosition() {
        if (mIsSelectRoute) {
            mCurrentVehicles.add(mCurrentRouteServerId);
        } else {
            mCurrentVehicles.remove(mCurrentRouteServerId);
        }

        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(RetrofitProvider.getRetrofit().getAllVehiclesForRoute(mCurrentVehicles)
                .subscribeOn(Schedulers.io())
                .repeatWhen(completed -> completed.delay(30, TimeUnit.SECONDS))
                .doOnError(this::errorHandle)
                .retryWhen(retryHandler -> retryHandler.delay(30, TimeUnit.SECONDS))
                .filter(listResponse -> !listResponse.equals(null))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse));
    }

    private void handleResponse(Response<List<VehiclesModel>> vehicles) {
        List<VehiclesModel> currentVehicles = vehicles.body();
        if (currentVehicles != null) {
            mMapsFragment.drawVehicles(currentVehicles);
        }
    }

    private void errorHandle(Throwable throwable) {
        if (mCurrentVehicles.isEmpty()) {
            mCompositeDisposable.dispose();
        }
        if (mIsSelectRoute) {
            if (throwable instanceof SocketTimeoutException) {
                mMapsFragment.showErrorSnackbar(R.string.snack_bar_no_vehicles_no_internet_connection);
            } else if (throwable instanceof ConnectException) {
                mMapsFragment.showErrorSnackbar(R.string.snack_bar_no_vehicles_no_internet_connection);
            }
        }
    }
}


