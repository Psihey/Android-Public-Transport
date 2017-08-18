package com.provectus.public_transport.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.provectus.public_transport.view.fragment.mapfragment.tabs.BusTabFragment;
import com.provectus.public_transport.view.fragment.mapfragment.tabs.ParkingTabFragment;
import com.provectus.public_transport.view.fragment.mapfragment.tabs.TramTabFragment;

/**
 * Created by Evgeniy on 8/17/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {


    private final Fragment[] fragments = new Fragment[3];
    private FragmentManager fragmentManager;


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;

        fragments[0] = BusTabFragment.newInstance(fragmentManager);
        fragments[1] = TramTabFragment.newInstance(fragmentManager);
        fragments[2] = ParkingTabFragment.newInstance(fragmentManager);
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
