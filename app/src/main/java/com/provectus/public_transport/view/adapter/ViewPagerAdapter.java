package com.provectus.public_transport.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.provectus.public_transport.view.fragment.mapfragment.RoutesTabFragment;

/**
 * Created by Evgeniy on 8/17/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public static final int POSITION_BUS = 0;
    public static final int POSITION_TRAM = 1;
    public static final int POSITION_PARKING = 2;

    private final Fragment[] fragments = new Fragment[3];
    private FragmentManager fragmentManager;


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;

        fragments[POSITION_BUS] = RoutesTabFragment.newInstance(POSITION_BUS);
        fragments[POSITION_TRAM] = RoutesTabFragment.newInstance(POSITION_TRAM);
        fragments[POSITION_PARKING] = RoutesTabFragment.newInstance(POSITION_PARKING);
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
