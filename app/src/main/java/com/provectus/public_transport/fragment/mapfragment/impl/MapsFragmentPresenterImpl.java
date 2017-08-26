package com.provectus.public_transport.fragment.mapfragment.impl;


import com.google.android.gms.maps.model.LatLng;
import com.orhanobut.logger.Logger;
import com.provectus.public_transport.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.model.PointEntity;
import com.provectus.public_transport.model.SegmentEntity;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.persistence.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by Evgeniy on 8/10/2017.
 */

public class MapsFragmentPresenterImpl implements MapsFragmentPresenter {

    private MapsFragment mMapsFragment;
    private List<SegmentEntity> mTransportEntity = new ArrayList<>();

    @Override
    public void bindView(MapsFragment mapsFragment) {
        mMapsFragment = mapsFragment;
        Logger.d("Maps is binded to its presenter.");
        DatabaseHelper.getPublicTransportDatabase().transportDao().get28Tram()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getTransportFromDB);
    }

    private void getTransportFromDB(List<TransportEntity> transportEntities) {
        for (TransportEntity current : transportEntities) {
            System.out.println(current);
        }
    }

    @Override
    public void unbindView() {
        mMapsFragment = null;
        Logger.d("Maps is unbind from presenter");
    }

    // TODO: 23.08.17 Use Rx
    @Deprecated
    private List<LatLng> sortedRoutesSegment(TransportEntity transportRoutes) {
        List<LatLng> listDirection1 = new ArrayList<>();
        List<LatLng> listDirection2 = new ArrayList<>();
        LatLng first = null;
        double lat = 0.0;
        double lng = 0.0;
        List<SegmentEntity> listSegment = transportRoutes.getSegments();
        for (int j = 0; j < listSegment.size(); j++) {
            List<PointEntity> pointList = listSegment.get(j).getPoints();
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
        List<LatLng> listRes = new ArrayList<>(listDirection1);
        listRes.addAll(listDirection2);
        return listRes;
    }

    private void sotr(List<TransportEntity> transportEntityList) {
        //Observable<List<TransportEntity>> query ();
    }
}


