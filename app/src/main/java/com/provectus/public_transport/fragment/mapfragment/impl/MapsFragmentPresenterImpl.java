package com.provectus.public_transport.fragment.mapfragment.impl;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.orhanobut.logger.Logger;
import com.provectus.public_transport.R;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.model.DirectEntity;
import com.provectus.public_transport.model.ParkingEntity;
import com.provectus.public_transport.model.StopEntity;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.VehicleMarkerInfoModel;
import com.provectus.public_transport.model.VehiclesModel;
import com.provectus.public_transport.model.converter.TransportType;
import com.provectus.public_transport.persistence.database.DatabaseHelper;
import com.provectus.public_transport.service.retrofit.RetrofitProvider;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.HttpURLConnection;
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
        EventBus.getDefault().postSticky(new BusEvents.SendMapsFragmentPresenter(this));
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
    public void getRouteInformation(TransportEntity transportEntity) {
        DatabaseHelper.getPublicTransportDatabase().transportDao().getChosenTransport(transportEntity.getServerId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Logger.d(throwable.getMessage()))
                .subscribe(this::getChosenTransportFromDB);
    }

    @Override
    public void stopGetVehicles() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        mCurrentVehicles.clear();
    }

    @Override
    public void getAllParking() {
        DatabaseHelper.getPublicTransportDatabase().parkingDao().getAllParkings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Logger.d(throwable.getMessage()))
                .subscribe(this::getAllParkingFromDB);
    }

    @Override
    public void onSelectCurrentRoute(TransportEntity event) {
        mIsSelectRoute = event.isSelected();
        String transportType = event.getType().toString();
        if (transportType.equals(TransportType.TROLLEYBUSES_TYPE.name())) {
            mTransportNumber = event.getNumber() + TROLLEY_NUMBER_INCREMENT;
        } else if (transportType.equals(TransportType.TRAM_TYPE.name())) {
            mTransportNumber = event.getNumber() + TRAM_NUMBER_INCREMENT;
        }
        mMapsFragment.getInfoTransport(mTransportNumber, event.getServerId());
        DatabaseHelper.getPublicTransportDatabase().transportDao().getTransportEntity(event.getNumber(), transportType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Logger.d(throwable.getMessage()))
                .subscribe(this::getTransportFromDB);
        if (mIsSelectRoute) {
            mMapsFragment.getColorForRoute();
            if (mMapsFragment.checkOnReadyMap()) {
                DatabaseHelper.getPublicTransportDatabase().transportDao().getStopsForCurrentTransport(event.getNumber(), transportType)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(throwable -> Logger.d(throwable.getMessage()))
                        .subscribe(this::getStopsFromDB);
                DatabaseHelper.getPublicTransportDatabase().transportDao().getDirectionEntity(event.getNumber(), transportType)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(throwable -> Logger.d(throwable.getMessage()))
                        .subscribe(this::getDirectionFromDB);

            } else {
                mMapsFragment.showErrorSnackbar(R.string.snack_bar_map_not_ready);
            }
        } else {
            mMapsFragment.removeStopsFromMap();
            mMapsFragment.removeRoutesWithDirectionFromMap();
        }
    }

    private void getAllParkingFromDB(List<ParkingEntity> parkingEntities) {
        if (parkingEntities != null && !parkingEntities.isEmpty()){
            mMapsFragment.addAllParkingToCluster(parkingEntities);
            return;
        }
        mMapsFragment.showErrorSnackbar(R.string.snack_bar_no_parking_data);
    }

    private void getChosenTransportFromDB(TransportEntity transportEntity) {
        if (mMapsFragment !=null && transportEntity!=null){
            mMapsFragment.openRouteInfo(transportEntity);
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
        mMapsFragment.drawStops(stopEntities);
    }

    private void getTransportFromDB(TransportEntity transportEntity) {
        VehicleMarkerInfoModel mVehicleMarker = new VehicleMarkerInfoModel();
        mVehicleMarker.setVehicleId(transportEntity.getServerId());
        mVehicleMarker.setDistance(transportEntity.getDistance());
        mVehicleMarker.setNumber(transportEntity.getNumber());
        mVehicleMarker.setServerId(transportEntity.getServerId());
        mCurrentRouteServerId = transportEntity.getServerId();
        if (mVehicleMarker != null) {
            mMapsFragment.sendVehicleInfo(mVehicleMarker);
        }
        getVehiclesPosition();
    }

    private void getVehiclesPosition() {
        if (mIsSelectRoute) {
            mCurrentVehicles.add(mCurrentRouteServerId);
        } else {
            mCurrentVehicles.remove(mCurrentRouteServerId);
        }
        if (mCurrentVehicles.size() == 0) {
            mMapsFragment.routeNotSelected();
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
        if (mIsSelectRoute) {
            if (vehicles.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                mMapsFragment.showErrorSnackbar(R.string.snack_bar_no_vehicles_for_this_route);
            }
        }
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
                mMapsFragment.showErrorSnackbar(R.string.snack_bar_no_vehicles_server_not_response);
            }
        }
    }
}