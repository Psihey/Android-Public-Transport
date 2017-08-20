package com.provectus.public_transport.view.fragment.mapfragment.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.provectus.public_transport.R;
import com.provectus.public_transport.view.adapter.ViewPagerAdapter;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragmentPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Evgeniy on 8/10/2017.
 */

public class MapsFragmentImpl extends Fragment implements MapsFragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private MapsFragmentPresenter mMapsPresenter;
    private Unbinder mUnbinder;
    private ViewPagerAdapter viewPagerAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (Build.VERSION.SDK_INT >= 23) {
            onAttachToContext(context);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < 23) {
            onAttachToContext(activity);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior.from(bottomSheet);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initViewPager();
        setIconInTabLayout();
        if (mMapsPresenter == null) {
            mMapsPresenter = new MapsFragmentPresenterImpl();

        }
        mMapsPresenter.bindView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapsPresenter.unbindView();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapsPresenter.unbindView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void showDialogError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_error_internet_title);
        builder.setMessage(R.string.dialog_error_internet_message);
        builder.setIcon(R.drawable.common_google_signin_btn_icon_dark_focused);
        builder.setPositiveButton(R.string.dialog_error_internet_positive_button, (dialog, which) -> {
            mMapsPresenter.getRoutesFromServer();
        });
        builder.setNegativeButton(R.string.dialog_error_internet_negative_button, (dialog, which) -> {
            getActivity().onBackPressed();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void setIconInTabLayout() {
        tabLayout.getTabAt(ViewPagerAdapter.POSITION_BUS).setIcon(R.drawable.ic_front_bus_blue);
        tabLayout.getTabAt(ViewPagerAdapter.POSITION_TRAM).setIcon(R.drawable.ic_tram_public_gray);
        tabLayout.getTabAt(ViewPagerAdapter.POSITION_PARKING).setIcon(R.drawable.ic_parking_gray);
    }

    @Override
    public void changeIconInTabLayout(int position) {
        tabLayout.getTabAt(ViewPagerAdapter.POSITION_BUS).setIcon(R.drawable.ic_front_bus_gray);
        tabLayout.getTabAt(ViewPagerAdapter.POSITION_TRAM).setIcon(R.drawable.ic_tram_public_gray);
        tabLayout.getTabAt(ViewPagerAdapter.POSITION_PARKING).setIcon(R.drawable.ic_parking_gray);
        switch (position) {
            case ViewPagerAdapter.POSITION_BUS:
                tabLayout.getTabAt(ViewPagerAdapter.POSITION_BUS).setIcon(R.drawable.ic_front_bus_blue);
                break;
            case ViewPagerAdapter.POSITION_TRAM:
                tabLayout.getTabAt(ViewPagerAdapter.POSITION_TRAM).setIcon(R.drawable.ic_tram_public_blue);
                break;
            case ViewPagerAdapter.POSITION_PARKING:
                tabLayout.getTabAt(ViewPagerAdapter.POSITION_PARKING).setIcon(R.drawable.ic_parking_blue);
                break;
        }
    }

    protected void onAttachToContext(Context context) {
        Context mContext = context;
    }

    private void initViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mMapsPresenter.changeViewPager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }
}
