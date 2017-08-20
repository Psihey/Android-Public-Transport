package com.provectus.public_transport.view.fragment.mapfragment.impl;


import com.orhanobut.logger.Logger;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragmentPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by Evgeniy on 8/10/2017.
 */

public class MapsFragmentPresenterImpl implements MapsFragmentPresenter {

    private MapsFragment mMapsFragment;

    @Override
    public void bindView(MapsFragment mapsFragment) {
        this.mMapsFragment = mapsFragment;
        EventBus.getDefault().register(this);
        Logger.d("Maps is binded to its presenter.");
    }

    @Override
    public void unbindView() {
        this.mMapsFragment = null;
        EventBus.getDefault().unregister(this);
        Logger.d("Maps is unbind from presenter");
    }

    @Override
    public void changeViewPager(int newPosition) {
        mMapsFragment.changeIconInTabLayout(newPosition);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAllRoutes(BusEvents.SendRoutesEvent routesEvent){
        // TODO : call routesEvent.getTransportRoutes and you will get all routes
        Logger.d("We got message from Event Bus with all routes ");
        System.out.println(routesEvent.getTransportRoutes().toString());
    }



}
