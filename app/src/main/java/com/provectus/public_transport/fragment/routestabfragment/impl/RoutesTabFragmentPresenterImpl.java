package com.provectus.public_transport.fragment.routestabfragment.impl;

import com.orhanobut.logger.Logger;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.fragment.routestabfragment.RoutesTabFragment;
import com.provectus.public_transport.fragment.routestabfragment.RoutesTabFragmentPresenter;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.converter.TransportType;
import com.provectus.public_transport.persistence.database.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RoutesTabFragmentPresenterImpl implements RoutesTabFragmentPresenter {

    private RoutesTabFragment mRoutesTabFragment;
    private TransportType mTransportType;

    @Override
    public void bindView(RoutesTabFragment routesTabFragment) {
        mRoutesTabFragment = routesTabFragment;
        EventBus.getDefault().register(this);
        Logger.d("RoutesTab is binded to its presenter.");
    }

    @Override
    public void unbindView() {
        mRoutesTabFragment = null;
        Logger.d("RoutesTab is unbind from presenter");
    }

    @Override
    public void setTransportType(TransportType transportType) {
        mTransportType = transportType;
        getDataFromDB();
    }

    @Override
    public void unregisteredEventBus() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateDBEvent(BusEvents.DataBaseInitialized routesEvent) {
        getDataFromDB();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateDBEvent(BusEvents.ServiceEndWorked service) {
        mRoutesTabFragment.serviceEndWorked();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateRecyclerView(BusEvents.UpdateDataTransportsRecyclerView transportEntity) {

        mRoutesTabFragment.updateData(transportEntity.getSelectRout());
    }

    private void getDataFromDB() {
        if (mTransportType == TransportType.TRAM_TYPE) {
            DatabaseHelper.getPublicTransportDatabase().transportDao().getAllTram()
                    .map(list -> {
                        Collections.sort(list, sortByNumber);
                        return list;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Logger.d(throwable.getMessage()))
                    .subscribe(this::getTransportFromDB);
        } else if (mTransportType == TransportType.TROLLEYBUSES_TYPE) {
            DatabaseHelper.getPublicTransportDatabase().transportDao().getAllTrolleybuses()
                    .map(list -> {
                        Collections.sort(list, sortByNumber);
                        return list;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Logger.d(throwable.getMessage()))
                    .subscribe(this::getTransportFromDB);
        }
    }

    private void getTransportFromDB(List<TransportEntity> transportEntities) {
        if (mRoutesTabFragment == null) {
            return;
        }
        if (transportEntities != null && !transportEntities.isEmpty()) {
            mRoutesTabFragment.initRecyclerView(transportEntities);
            mRoutesTabFragment.serviceEndWorked();
        } else {
            mRoutesTabFragment.checkMyServiceRunning();

        }

    }

    private Comparator<TransportEntity> sortByNumber = (t1, t2) -> t1.getNumber() > t2.getNumber() ? 1 : -1;

}