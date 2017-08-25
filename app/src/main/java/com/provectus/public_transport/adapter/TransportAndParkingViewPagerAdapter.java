package com.provectus.public_transport.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.provectus.public_transport.fragment.routestabfragment.impl.RoutesTabFragmentImpl;
import com.provectus.public_transport.model.TransportType;

/**
 * Created by Evgeniy on 8/17/2017.
 */

public class TransportAndParkingViewPagerAdapter extends FragmentPagerAdapter {

    public static final int POSITION_BUS = 0;
    public static final int POSITION_TRAM = 1;
    public static final int POSITION_PARKING = 2;

    private final Fragment[] fragments = new Fragment[3];

    public TransportAndParkingViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[POSITION_BUS] = RoutesTabFragmentImpl.newInstance(TransportType.TROLLEYBUSES_TYPE);
        fragments[POSITION_TRAM] = RoutesTabFragmentImpl.newInstance(TransportType.TRAM_TYPE);
        fragments[POSITION_PARKING] = RoutesTabFragmentImpl.newInstance(TransportType.PARKING_TYPE);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

}
