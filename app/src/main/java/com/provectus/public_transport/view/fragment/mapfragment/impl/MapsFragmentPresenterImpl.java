package com.provectus.public_transport.view.fragment.mapfragment.impl;


import com.orhanobut.logger.Logger;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.model.TransportRoutes;
import com.provectus.public_transport.service.RetrofitProvider;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragmentPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        Logger.d("All Ok, we got responce");
        EventBus.getDefault().post(new BusEvents.SendRoutesEvent(transportRoutes));
      
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
    }

    private void handleError(Throwable throwable) {
        mMapsFragment.showDialogError();
        Logger.d("Handle Error from when fetching data" + throwable.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAllRoutes(BusEvents.SendRoutesEvent routesEvent){
        // TODO : call routesEvent.getTransportRoutes and you will get all routes
        Logger.d("We got message from Event Bus with all routes ");
    }
}
