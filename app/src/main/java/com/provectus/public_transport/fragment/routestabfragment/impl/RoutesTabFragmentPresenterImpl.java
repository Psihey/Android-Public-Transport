package com.provectus.public_transport.fragment.routestabfragment.impl;

import com.provectus.public_transport.fragment.routestabfragment.RoutesTabFragment;
import com.provectus.public_transport.fragment.routestabfragment.RoutesTabFragmentPresenter;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.TransportType;
import com.provectus.public_transport.persistence.database.DatabaseHelper;

import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Evgeniy on 8/23/2017.
 */

public class RoutesTabFragmentPresenterImpl implements RoutesTabFragmentPresenter {

    private RoutesTabFragment mRoutesTabFragment;
    private TransportType mTransportType;

    @Override
    public void bindView(RoutesTabFragment routesTabFragment) {
        mRoutesTabFragment = routesTabFragment;
    }

    @Override
    public void unbindView() {
        mRoutesTabFragment = null;
    }

    @Override
    public void setTransportType(TransportType transportType) {
        mTransportType = transportType;
        getDataFromDB();
    }

    private void getDataFromDB() {
        if (mTransportType == TransportType.TRAM_TYPE) {
            DatabaseHelper.getPublicTransportDatabase().transportDao().getAllTram()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::getTransportFromDB);
        } else if (mTransportType == TransportType.TROLLEYBUSES_TYPE) {
            DatabaseHelper.getPublicTransportDatabase().transportDao().getAllTrolleybuses()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::getTransportFromDB);
        }
    }

    private void getTransportFromDB(List<TransportEntity> transportEntities) {
        sortByRouteNumber(transportEntities);
        mRoutesTabFragment.initRecyclerView(transportEntities);
    }

    private void sortByRouteNumber(List<TransportEntity> transportEntities) {
        Collections.sort(transportEntities, (t1, t2) -> {
            int res = 0;
            if (t1.getNumber() > t2.getNumber()) {
                res = 1;
            } else if (t1.getNumber() < t2.getNumber()) {
                res = -1;
            }
            return res;
        });
    }

}
