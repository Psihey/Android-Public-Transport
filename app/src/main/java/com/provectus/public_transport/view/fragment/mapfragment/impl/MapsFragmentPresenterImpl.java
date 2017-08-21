package com.provectus.public_transport.view.fragment.mapfragment.impl;


import com.google.android.gms.maps.model.LatLng;
import com.orhanobut.logger.Logger;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.model.Point;
import com.provectus.public_transport.model.Segment;
import com.provectus.public_transport.model.TransportRoutes;
import com.provectus.public_transport.model.TransportType;
import com.provectus.public_transport.persistence.database.DatabaseHelper;
import com.provectus.public_transport.persistence.entity.PointEntity;
import com.provectus.public_transport.persistence.entity.SegmentEntity;
import com.provectus.public_transport.persistence.entity.TransportEntity;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragmentPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Evgeniy on 8/10/2017.
 */

public class MapsFragmentPresenterImpl implements MapsFragmentPresenter {

    private MapsFragment mMapsFragment;

    @Override
    public void bindView(MapsFragment mapsFragment) {
        this.mMapsFragment = mapsFragment;
        EventBus.getDefault().register(this);
        Logger.d("Maps is binded to its presenter.");
    }

    @Override
    public void unbindView() {
        this.mMapsFragment = null;
        EventBus.getDefault().unregister(this);
        Logger.d("Maps is unbind from presenter");
    }

    @Override
    public void changeViewPager(int newPosition) {
        mMapsFragment.changeIconInTabLayout(newPosition);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAllRoutes(BusEvents.SendRoutesEvent routesEvent) {
        Logger.d("We got message from Event Bus with all routes ");
        List<TransportRoutes> routes = routesEvent.getTransportRoutes();
        for (int i = 0; i < routes.size(); i++) {
            List<LatLng> sortedRoutes = sortedRoutesSegment(routes.get(i));
            mMapsFragment.drawRotes(sortedRoutes);
        }

        DatabaseHelper.getPublicTransportDatabase().transportDao().getAllTransport()
                .subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getTransportFromDB);
        DatabaseHelper.getPublicTransportDatabase().segmentDao().getAllSegment()
                .subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getSegmentFromDB);
        DatabaseHelper.getPublicTransportDatabase().pointDao().getAllPoint()
                .subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getPoints);

    }

    private void getTransportFromDB(List<TransportEntity> transportEntities) {
        Logger.d(transportEntities);
    }

    private void getSegmentFromDB(List<SegmentEntity> segmentEntities) {
        Logger.d(segmentEntities);
    }

    private void getPoints(List<PointEntity> pointEntities) {
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
