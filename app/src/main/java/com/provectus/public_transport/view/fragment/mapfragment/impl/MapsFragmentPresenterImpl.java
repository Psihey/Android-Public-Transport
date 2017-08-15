package com.provectus.public_transport.view.fragment.mapfragment.impl;


import com.orhanobut.logger.Logger;
import com.provectus.public_transport.model.TransportRoutes;
import com.provectus.public_transport.service.RetrofitProvider;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Evgeniy on 8/10/2017.
 */

public class MapsFragmentPresenterImpl implements MapsFragmentPresenter {

    private MapsFragment mapsFragment;
    private CompositeDisposable mCompositeDisposable;
    private final static String TAXI_TYPE = "taxi";

    @Override
    public void bindView(MapsFragment mapsFragment) {
        this.mapsFragment = mapsFragment;
        mCompositeDisposable = new CompositeDisposable();
        getRoutesFromServer();
        Logger.d("Maps is binded to its presenter.");
    }

    @Override
    public void unbindView() {
        this.mapsFragment = null;
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        Logger.d("Maps is unbind from presenter");
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
        List<TransportRoutes> routes = new ArrayList<>();
        for (TransportRoutes transportRoutes1 : transportRoutes) {
            //TODO : Think about it! How we can improve this!
            if (!transportRoutes1.getType().equals(TAXI_TYPE) && transportRoutes1.getId() > 2) {
                routes.add(transportRoutes1);
            }
        }
        mapsFragment.initRecyclerView(routes);
        Logger.d("All Ok, we got responce");
    }

    private void handleError(Throwable throwable) {
        mapsFragment.showDialogError();
        Logger.d("Handle Error from when fetching data");
    }

}
