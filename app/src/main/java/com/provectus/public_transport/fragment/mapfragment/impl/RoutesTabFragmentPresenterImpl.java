package com.provectus.public_transport.fragment.mapfragment.impl;

import com.provectus.public_transport.fragment.mapfragment.RoutesTabFragment;
import com.provectus.public_transport.fragment.mapfragment.RoutesTabFragmentPresenter;

/**
 * Created by Evgeniy on 8/23/2017.
 */

public class RoutesTabFragmentPresenterImpl implements RoutesTabFragmentPresenter {

    private RoutesTabFragment mRoutesTabFragment;

    @Override
    public void bindView(RoutesTabFragment routesTabFragment) {
        this.mRoutesTabFragment = routesTabFragment;

    }

    @Override
    public void unbindView() {
        mRoutesTabFragment = null;
    }




}
