package com.provectus.public_transport.fragment.routestabfragment.impl;

import com.orhanobut.logger.Logger;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.persistence.database.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class TramFragmentPresenterImpl implements com.provectus.public_transport.fragment.routestabfragment.TramFragmentPresenter {
    private TramFragmentImpl mTramFragment;

    @Override
    public void bindView(TramFragmentImpl tramFragment) {
        mTramFragment = tramFragment;
        EventBus.getDefault().register(this);
        EventBus.getDefault().postSticky(new BusEvents.SendTramFragmentPresenter(this));
        getDataFromDB();
        Logger.d("RoutesTab is binded to its presenter.");
    }

    @Override
    public void unbindView() {
        mTramFragment = null;
        Logger.d("RoutesTab is unbind from presenter");
    }

    @Override
    public void unregisteredEventBus() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getDataForUpdateRecyclerView(TransportEntity transportEntity) {
        mTramFragment.updateData(transportEntity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateDBEvent(BusEvents.DataBaseInitialized routesEvent) {
        getDataFromDB();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateDBEvent(BusEvents.ServiceEndWorked service) {
        mTramFragment.serviceEndWorked();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateRecyclerView(BusEvents.UnselectedAllItems updateRecyclerView) {
        getDataFromDB();
    }

    private void getDataFromDB() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().getAllTram()
                .map(list -> {
                    Collections.sort(list, sortByNumber);
                    return list;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Logger.d(throwable.getMessage()))
                .subscribe(this::getTransportFromDB);
    }

    private void getTransportFromDB(List<TransportEntity> transportEntities) {
        if (mTramFragment == null) {
            return;
        }
        if (transportEntities != null && !transportEntities.isEmpty()) {
            mTramFragment.initRecyclerView(transportEntities);
            mTramFragment.serviceEndWorked();
        } else {
            mTramFragment.checkMyServiceRunning();
        }
    }

    private Comparator<TransportEntity> sortByNumber = (t1, t2) -> t1.getNumber() > t2.getNumber() ? 1 : -1;

}
