package com.provectus.public_transport.fragment.mapfragment.impl;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.orhanobut.logger.Logger;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.model.PointEntity;
import com.provectus.public_transport.model.SegmentEntity;
import com.provectus.public_transport.model.StopEntity;
import com.provectus.public_transport.persistence.database.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by Evgeniy on 8/10/2017.
 */

public class MapsFragmentPresenterImpl implements MapsFragmentPresenter {

    private MapsFragment mMapsFragment;
    private List<SegmentEntity> mSegmentsDataForCurrentRoute = new ArrayList<>();
    private List<PointEntity> mPointsDataForCurrentRoute = new ArrayList<>();
    private List<StopEntity> mStopsDataForCurrentRoute = new ArrayList<>();
    private List<SegmentEntity> mSegmentsWithPointsForCurrentRoute = new ArrayList<>();
    private boolean mIsSelectRoute;
    private int number;
    private Map<Integer,PolylineOptions> currentRoute = new ConcurrentHashMap<>();
    private Map<Integer,List<MarkerOptions>> currentOptions = new ConcurrentHashMap<>();


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
        mSegmentsWithPointsForCurrentRoute.clear();
        mSegmentsDataForCurrentRoute.clear();
        mPointsDataForCurrentRoute.clear();
        mStopsDataForCurrentRoute.clear();
        mIsSelectRoute = event.getSelectRout().isIsSelected();
        String transportType = event.getSelectRout().getType().toString();
        Logger.d(transportType);
        if (transportType.equals("TROLLEYBUSES_TYPE")){
            number = event.getSelectRout().getNumber()+100;
        }else if(transportType.equals("TRAM_TYPE")){
            number=event.getSelectRout().getNumber()+1000;
        }
        DatabaseHelper.getPublicTransportDatabase().transportDao().getSegmentForCurrentTransport(event.getSelectRout().getNumber(), transportType)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Logger.d(throwable.getMessage()))
                .subscribe(this::getSegmentsFromDB);
        DatabaseHelper.getPublicTransportDatabase().transportDao().getStopsForCurrentTransport(event.getSelectRout().getNumber(), transportType)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Logger.d(throwable.getMessage()))
                .subscribe(this::getStopsFromDB);
        DatabaseHelper.getPublicTransportDatabase().transportDao().getPointsForCurrentTransport(event.getSelectRout().getNumber(), transportType)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Logger.d(throwable.getMessage()))
                .subscribe(this::getPointsFromDB);
    }


    private void getSegmentsFromDB(List<SegmentEntity> segmentEntities) {
        mSegmentsDataForCurrentRoute.addAll(segmentEntities);
    }

    private void getStopsFromDB(List<StopEntity> stopEntities) {
        mStopsDataForCurrentRoute.addAll(stopEntities);
    }

    private void getPointsFromDB(List<PointEntity> pointEntities) {
        mPointsDataForCurrentRoute.addAll(pointEntities);
        EventBus.getDefault().post(new BusEvents.DataForCurrentRouteFetched());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void connectPointsToSegments(BusEvents.DataForCurrentRouteFetched event) {
        for (SegmentEntity currentSegment : mSegmentsDataForCurrentRoute) {
            List<PointEntity> finals = new ArrayList<>();
            for (PointEntity currentPoint : mPointsDataForCurrentRoute) {
                if (currentSegment.getServerId() == currentPoint.getSegmentId()) {
                    finals.add(currentPoint);
                }
            }
            mSegmentsWithPointsForCurrentRoute.add(new SegmentEntity(currentSegment.getDirection(), currentSegment.getPosition(), finals));
        }
        mMapsFragment.drawRotes(sortedRoutesSegment(mSegmentsWithPointsForCurrentRoute), getStopsOnRoute(mStopsDataForCurrentRoute));
    }

    // TODO: 23.08.17 Use Rx
    private Map<Integer,PolylineOptions> sortedRoutesSegment(List<SegmentEntity> segmentEntities) {
        List<LatLng> listDirection1 = new ArrayList<>();
        List<LatLng> listDirection2 = new ArrayList<>();
        LatLng first = null;
        double lat = 0.0;
        double lng = 0.0;
        for (int j = 0; j < segmentEntities.size(); j++) {
            List<PointEntity> pointList = segmentEntities.get(j).getPoints();
            for (int r = 0; r < pointList.size(); r++) {
                lat = pointList.get(r).getLatitude();
                lng = pointList.get(r).getLongitude();
            }
            if (lng == lat) {
                continue;
            }
            if (segmentEntities.get(j).getDirection() == -1 && segmentEntities.get(j).getPosition() == -1) {
                //This is the beginning of the segment route with direction "1"
                first = new LatLng(lat, lng);
                listDirection1.add(0, new LatLng(lat, lng));
            } else if (segmentEntities.get(j).getDirection() == -1 && segmentEntities.get(j).getPosition() == 0) {
                //This is the beginning of the segment route with direction "0"
                listDirection2.add(0, new LatLng(lat, lng));
            }
            if (segmentEntities.get(j).getDirection() == 1) {
                listDirection1.add(new LatLng(lat, lng));
            } else if (segmentEntities.get(j).getDirection() == 0) {
                listDirection2.add(new LatLng(lat, lng));
            }
        }
        if (first != null) {
            listDirection2.add(first);
        }
        List<LatLng> listRes = new ArrayList<>(listDirection1);
        listRes.addAll(listDirection2);

        PolylineOptions  polylineOptions = new PolylineOptions().addAll(listRes);
        Logger.d(number);
        Logger.d(polylineOptions);
        if (mIsSelectRoute){
            currentRoute.put(number,polylineOptions);
        }else {
            for (Map.Entry<Integer,PolylineOptions> entry : currentRoute.entrySet()){
                Logger.d("ffffffffff");
                Integer key = entry.getKey();
                if (key!= number){

                }else{
                    Logger.d("addddddddd");
                    currentRoute.remove(number);
                }
            }
        }
        return  currentRoute;
    }

    private Map<Integer,List<MarkerOptions>> getStopsOnRoute(List<StopEntity> stopEntities) {
        List<MarkerOptions> markerOption = new ArrayList<>();
        for (int i = 0; i < stopEntities.size(); i++) {
            double lat = stopEntities.get(i).getLatitude();
            double lng = stopEntities.get(i).getLongitude();
            markerOption.add(new MarkerOptions().position(new LatLng(lat,lng)));
        }

        if (mIsSelectRoute){
            currentOptions.put(number,markerOption);
        }else {
            for (Map.Entry<Integer,List<MarkerOptions>> entry : currentOptions.entrySet()){
                Logger.d("ffffffffff");
                Integer key = entry.getKey();
                if (key!= number){

                }else{
                    Logger.d("addddddddd");
                    currentOptions.remove(number);
                }
            }
        }

        return currentOptions;
    }

}


