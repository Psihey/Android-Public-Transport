package com.provectus.public_transport.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.provectus.public_transport.view.fragment.mapfragment.tabs.TabLayoutFragment;
import com.provectus.public_transport.view.util.consts.Constants;

/**
 * Created by Evgeniy on 8/17/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {


    private final Fragment[] fragments = new Fragment[3];
    private FragmentManager fragmentManager;


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;

        fragments[Constants.TabPosition.BUS] = TabLayoutFragment.newInstance(fragmentManager, Constants.TabPosition.BUS);
        fragments[Constants.TabPosition.TRAM] = TabLayoutFragment.newInstance(fragmentManager, Constants.TabPosition.TRAM);
        fragments[Constants.TabPosition.PARKING] = TabLayoutFragment.newInstance(fragmentManager, Constants.TabPosition.PARKING);
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
