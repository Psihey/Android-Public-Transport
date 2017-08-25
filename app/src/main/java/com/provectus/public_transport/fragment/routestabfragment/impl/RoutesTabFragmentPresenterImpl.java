package com.provectus.public_transport.fragment.routestabfragment.impl;

import com.provectus.public_transport.fragment.routestabfragment.RoutesTabFragment;
import com.provectus.public_transport.fragment.routestabfragment.RoutesTabFragmentPresenter;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.TransportType;
import com.provectus.public_transport.persistence.database.DatabaseHelper;

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
        this.mRoutesTabFragment = routesTabFragment;
    }

    @Override
    public void unbindView() {
        mRoutesTabFragment = null;
    }

    @Override
    public void setTransportType(TransportType transportType) {
        mTransportType = transportType;
        getDatafromDB();
    }

    private void getDatafromDB() {
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
        mRoutesTabFragment.initRecyclerView(transportEntities);
    }

}
