package com.provectus.public_transport.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.provectus.public_transport.fragment.ParkingFragment;
import com.provectus.public_transport.fragment.routestabfragment.impl.TramFragmentImpl;
import com.provectus.public_transport.fragment.routestabfragment.impl.TrolleybusFragmentImpl;
import com.provectus.public_transport.fragment.favouritesfragment.impl.FavouritesFragmentImpl;

public class TransportAndParkingViewPagerAdapter extends FragmentPagerAdapter {

    public static final int POSITION_BUS = 0;
    public static final int POSITION_TRAM = 1;
    public static final int POSITION_PARKING = 2;
    public static final int POSITION_FAVOURITES = 3;

    private final Fragment[] fragments = new Fragment[4];

    public TransportAndParkingViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[POSITION_BUS] = new TrolleybusFragmentImpl();
        fragments[POSITION_TRAM] = new TramFragmentImpl();
        fragments[POSITION_PARKING] =  new ParkingFragment();
        fragments[POSITION_FAVOURITES] = new FavouritesFragmentImpl();
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
