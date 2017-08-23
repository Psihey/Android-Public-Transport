package com.provectus.public_transport.view.fragment.mapfragment.impl;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.provectus.public_transport.R;
import com.provectus.public_transport.service.TransportRoutesService;
import com.provectus.public_transport.view.adapter.ViewPagerAdapter;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragmentPresenter;

import java.util.List;
import java.util.Random;

import biz.laenger.android.vpbs.BottomSheetUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Evgeniy on 8/10/2017.
 */

public class MapsFragmentImpl extends Fragment implements MapsFragment, OnMapReadyCallback {

    // TODO: 23.08.17 Move and rename it
    private static final Double LAT_1 = 46.348612;
    private static final Double LNG_1 = 30.671341;
    private static final Double LAT_2 = 46.499907;
    private static final Double LNG_2 = 30.781572;
    private static final Integer MARGE = 30;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private MapsFragmentPresenter mMapsPresenter;
    private Unbinder mUnbinder;
    private ViewPagerAdapter viewPagerAdapter;
    private GoogleMap mMap;

    private boolean isMapReady;

    // TODO: 23.08.17 Remove it
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (Build.VERSION.SDK_INT >= 23) {
            onAttachToContext(context);
        }
    }

    // TODO: 23.08.17 Remove it
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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initViewPager();
        setIconInTabLayout();
        getActivity().startService(new Intent(getContext(), TransportRoutesService.class));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        getActivity().stopService(new Intent(getContext(), TransportRoutesService.class));
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;
        mMap = googleMap;
        setDefaultCameraPosition();
        settingsUI();
    }

    @Override
    public void drawRotes(List<LatLng> sortedRoutes) {
        if (isMapReady == false || mMap == null || sortedRoutes == null) {
            return;
        }
        setRoutesOnMap(sortedRoutes);
    }

    // TODO: 23.08.17 Remove it
    protected void onAttachToContext(Context context) {
        Context mContext = context;
    }

    private void setRoutesOnMap(List<LatLng> listDirection) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(listDirection);
        Polyline polyline = mMap.addPolyline(polylineOptions);
        polyline.setColor(getRandomColor());
        polyline.setWidth(3);
    }

    // TODO: 23.08.17 Move to Utils class
    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private void initViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setOffscreenPageLimit(3);
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


        BottomSheetUtils.setupViewPager(viewPager);
    }

    private void setDefaultCameraPosition() {
        mMap.setOnMapLoadedCallback(() -> {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(new LatLngBounds
                    (new LatLng(LAT_1, LNG_1), new LatLng(LAT_2, LNG_2)), MARGE);
            mMap.animateCamera(cameraUpdate);
        });
    }

    // TODO: 23.08.17 Rename it
    private void settingsUI() {
        UiSettings settings = mMap.getUiSettings();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: 23.08.17 Implement it
            return;
        }
        mMap.setMyLocationEnabled(true);
        settings.setMyLocationButtonEnabled(true);
    }

}
