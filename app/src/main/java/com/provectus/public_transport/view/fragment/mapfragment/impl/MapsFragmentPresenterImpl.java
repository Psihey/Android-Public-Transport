package com.provectus.public_transport.view.fragment.mapfragment.impl;


import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.orhanobut.logger.Logger;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.model.Point;
import com.provectus.public_transport.model.Segment;
import com.provectus.public_transport.model.TransportRoutes;
import com.provectus.public_transport.service.RetrofitProvider;
import com.provectus.public_transport.view.adapter.ViewPagerAdapter;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragmentPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Evgeniy on 8/10/2017.
 */

public class MapsFragmentPresenterImpl implements MapsFragmentPresenter {

    private MapsFragment mMapsFragment;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void bindView(MapsFragment mapsFragment) {
        this.mMapsFragment = mapsFragment;
        mCompositeDisposable = new CompositeDisposable();
        getRoutesFromServer();
        EventBus.getDefault().register(this);
        Logger.d("Maps is binded to its presenter.");

    }

    @Override
    public void unbindView() {
        this.mMapsFragment = null;
        EventBus.getDefault().unregister(this);
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        Logger.d("Maps is unbind from presenter");
    }

    @Override
    public void changeViewPager(int newPosition) {
        mMapsFragment.changeIconInTabLayout(newPosition);
    }

    @Override
    public void getRoutesFromServer() {
        mCompositeDisposable.add(RetrofitProvider
                .getRetrofit().getAllRoutes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(List<TransportRoutes> transportRoutes) {
        Logger.d("All Ok, we got response");
        List<TransportRoutes> busRoutes = new ArrayList<>();
        List<TransportRoutes> tramRoutes = new ArrayList<>();
        for (TransportRoutes currentRoutes : transportRoutes) {
            switch (currentRoutes.getType()) {
                case TROLLEYBUSES_TYPE:
                    busRoutes.add(currentRoutes);
                    break;
                case TRAM_TYPE:
                    tramRoutes.add(currentRoutes);
                    break;
            }
        }
        EventBus.getDefault().post(new BusEvents.SendRoutesEvent(busRoutes, ViewPagerAdapter.POSITION_BUS));
        EventBus.getDefault().post(new BusEvents.SendRoutesEvent(tramRoutes, ViewPagerAdapter.POSITION_TRAM));
    }

    private void handleError(Throwable throwable) {
        mMapsFragment.showDialogError();
        Logger.d("Handle Error from when fetching data" + throwable.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAllRoutes(BusEvents.SendRoutesEvent routesEvent) {
        Logger.d("We got message from Event Bus with all routes");
        List<TransportRoutes> routes = routesEvent.getTransportRoutes();
        for (int i = 0; i < routes.size(); i++) {
            List<LatLng> sortedRoutes = sortedRoutesSegment(routes.get(i));
            mMapsFragment.drawRotes(sortedRoutes);
        }
    }

    private List<LatLng> sortedRoutesSegment(TransportRoutes transportRoutes) {
        List<LatLng> listDirection1 = new ArrayList<>();
        List<LatLng> listDirection2 = new ArrayList<>();
        LatLng first = null;
        double lat = 0.0;
        double lng = 0.0;
        List<Segment> listSegment = transportRoutes.getSegment();
        for (int j = 0; j < listSegment.size(); j++) {
            List<Point> pointList = listSegment.get(j).getPoints();
            for (int r = 0; r < pointList.size(); r++) {
                lat = pointList.get(r).getLatitude();
                lng = pointList.get(r).getLongitude();
            }
            if (lng == lat) {
                continue;
            }
            if (listSegment.get(j).getDirection() == -1 && listSegment.get(j).getPosition() == -1) {
                //This is the beginning of the segment route with direction "1"
                first = new LatLng(lat, lng);
                listDirection1.add(0, new LatLng(lat, lng));
            } else if (listSegment.get(j).getDirection() == -1 && listSegment.get(j).getPosition() == 0) {
                //This is the beginning of the segment route with direction "0"
                listDirection2.add(0, new LatLng(lat, lng));
            }
            if (listSegment.get(j).getDirection() == 1) {
                listDirection1.add(new LatLng(lat, lng));
            } else if (listSegment.get(j).getDirection() == 0) {
                listDirection2.add(new LatLng(lat, lng));
            }
        }
        if (first != null) {
            listDirection2.add(first);
        }
        listDirection1.addAll(listDirection2);
        return listDirection1;
    }

}
