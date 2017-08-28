package com.provectus.public_transport.fragment.mapfragment.impl;


import com.google.android.gms.maps.model.LatLng;
import com.orhanobut.logger.Logger;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.model.PointEntity;
import com.provectus.public_transport.model.SegmentEntity;
import com.provectus.public_transport.persistence.database.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by Evgeniy on 8/10/2017.
 */

public class MapsFragmentPresenterImpl implements MapsFragmentPresenter {

    private MapsFragment mMapsFragment;
    private List<SegmentEntity> mSegmentEntity = new ArrayList<>();
    private List<PointEntity> mPointEntity = new ArrayList<>();
    private List<SegmentEntity> finalData = new ArrayList<>();

    @Override
    public void bindView(MapsFragment mapsFragment) {
        mMapsFragment = mapsFragment;
        Logger.d("Maps is binded to its presenter.");
        EventBus.getDefault().register(this);
    }

    @Override
    public void unbindView() {
        mMapsFragment = null;
        EventBus.getDefault().unregister(this);
        Logger.d("Maps is unbind from presenter");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BusEvents.SendChosenRouter event) {
        finalData.clear();
        mSegmentEntity.clear();
        mPointEntity.clear();
        switch (event.getmTransportEntity().getType()) {
            case TROLLEYBUSES_TYPE:
                DatabaseHelper.getPublicTransportDatabase().transportDao().getSegmentForCurrentTrolley(event.getmTransportEntity().getNumber())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::getSegmnetsTrolleyFromDB);
                DatabaseHelper.getPublicTransportDatabase().transportDao().getPointsForCurrentTrolley(event.getmTransportEntity().getNumber())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::getPointsTrolleyFromDB);
                break;
            case TRAM_TYPE:
                DatabaseHelper.getPublicTransportDatabase().transportDao().getSegmentForCurrentTram(event.getmTransportEntity().getNumber())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::getSegmentsTramFromDB);
                DatabaseHelper.getPublicTransportDatabase().transportDao().getPointsForCurrentTram(event.getmTransportEntity().getNumber())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::getPointsTramFromDB);
        }
    }

    private void getSegmentsTramFromDB(List<SegmentEntity> transportEntities) {
        for (SegmentEntity current : transportEntities) {
            mSegmentEntity.add(current);
        }
    }

    private void getSegmnetsTrolleyFromDB(List<SegmentEntity> transportEntities) {
        for (SegmentEntity current : transportEntities) {
            mSegmentEntity.add(current);
        }
    }

    private void getPointsTramFromDB(List<PointEntity> transportEntities) {
        for (PointEntity current : transportEntities) {
            mPointEntity.add(current);
        }
        connectPointsToSegments();
    }

    private void getPointsTrolleyFromDB(List<PointEntity> transportEntities) {
        for (PointEntity current : transportEntities) {
            mPointEntity.add(current);
        }
        connectPointsToSegments();
    }

    private void connectPointsToSegments() {
        for (SegmentEntity currentSegment : mSegmentEntity) {
            List<PointEntity> finals = new ArrayList<>();
            for (PointEntity currentPoint : mPointEntity) {
                if (currentSegment.getServerId() == currentPoint.getSegmentId()) {
                    finals.add(currentPoint);
                }
            }
            finalData.add(new SegmentEntity(currentSegment.getDirection(), currentSegment.getPosition(), finals));
        }
        mMapsFragment.drawRotes(sortedRoutesSegment(finalData));

    }

    // TODO: 23.08.17 Use Rx
    private List<LatLng> sortedRoutesSegment(List<SegmentEntity> transportRoutes) {
        List<LatLng> listDirection1 = new ArrayList<>();
        List<LatLng> listDirection2 = new ArrayList<>();
        LatLng first = null;
        double lat = 0.0;
        double lng = 0.0;
        for (int j = 0; j < transportRoutes.size(); j++) {
            List<PointEntity> pointList = transportRoutes.get(j).getPoints();
            for (int r = 0; r < pointList.size(); r++) {
                lat = pointList.get(r).getLatitude();
                lng = pointList.get(r).getLongitude();
            }
            if (lng == lat) {
                continue;
            }
            if (transportRoutes.get(j).getDirection() == -1 && transportRoutes.get(j).getPosition() == -1) {
                //This is the beginning of the segment route with direction "1"
                first = new LatLng(lat, lng);
                listDirection1.add(0, new LatLng(lat, lng));
            } else if (transportRoutes.get(j).getDirection() == -1 && transportRoutes.get(j).getPosition() == 0) {
                //This is the beginning of the segment route with direction "0"
                listDirection2.add(0, new LatLng(lat, lng));
            }
            if (transportRoutes.get(j).getDirection() == 1) {
                listDirection1.add(new LatLng(lat, lng));
            } else if (transportRoutes.get(j).getDirection() == 0) {
                listDirection2.add(new LatLng(lat, lng));
            }
        }
        if (first != null) {
            listDirection2.add(first);
        }
        List<LatLng> listRes = new ArrayList<>(listDirection1);
        listRes.addAll(listDirection2);
        return listRes;
    }

}


