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

import static com.provectus.public_transport.model.TransportType.TAXI_TYPE;


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
        Logger.d("Maps is binded to its presenter.");
    }

    @Override
    public void unbindView() {
        this.mMapsFragment = null;
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
        for (TransportRoutes currentRoutes : transportRoutes) {
            //TODO : Think about it! How we can improve this!
            if ((currentRoutes.getType()!= TAXI_TYPE) && currentRoutes.getId() > 2) {
                routes.add(currentRoutes);
                System.out.println(currentRoutes.getType());
            }
        }
        mMapsFragment.initRecyclerView(routes);
        Logger.d("All Ok, we got responce");
    }

    private void handleError(Throwable throwable) {
        mMapsFragment.showDialogError();
        Logger.d("Handle Error from when fetching data");
    }

}
