package com.provectus.public_transport.fragment.mapfragment.impl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.provectus.public_transport.adapter.ViewPagerAdapter;
import com.provectus.public_transport.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.service.TransportRoutesService;
import com.provectus.public_transport.utils.Const;

import java.util.List;

import biz.laenger.android.vpbs.BottomSheetUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.provectus.public_transport.utils.Utils.getRandomColor;

/**
 * Created by Evgeniy on 8/10/2017.
 */

public class MapsFragmentImpl extends Fragment implements MapsFragment, OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSIONS = 1;

    @BindView(R.id.bottom_sheet_view_pager)
    ViewPager mViewPager;

    @BindView(R.id.bottom_sheet_tab_layout)
    TabLayout mBottomSheetTabLayout;

    private MapsFragmentPresenter mMapsPresenter;
    private Unbinder mUnbinder;
    private ViewPagerAdapter mPagerAdapter;
    private GoogleMap mMap;

    private boolean isMapReady;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initViewPager();
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
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;
        mMap = googleMap;
        setDefaultCameraPosition();
        setMyLocationButton();
    }

    @Override
    public void drawRotes(List<LatLng> sortedRoutes) {
        if (!isMapReady || mMap == null || sortedRoutes == null) {
            return;
        }
        setRoutesOnMap(sortedRoutes);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 2 && requestCode == REQUEST_LOCATION_PERMISSIONS) {
            setMyLocationButton();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setRoutesOnMap(List<LatLng> listDirection) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(listDirection);
        Polyline polyline = mMap.addPolyline(polylineOptions);
        polyline.setColor(getRandomColor());
        polyline.setWidth(3);
    }

    private void initViewPager() {
        mPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeIconInTabLayout(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mBottomSheetTabLayout.setupWithViewPager(mViewPager);
        mBottomSheetTabLayout.getTabAt(ViewPagerAdapter.POSITION_BUS).setIcon(R.drawable.ic_front_bus_blue);
        mBottomSheetTabLayout.getTabAt(ViewPagerAdapter.POSITION_TRAM).setIcon(R.drawable.ic_tram_public_gray);
        mBottomSheetTabLayout.getTabAt(ViewPagerAdapter.POSITION_PARKING).setIcon(R.drawable.ic_parking_gray);
        BottomSheetUtils.setupViewPager(mViewPager);
    }

    public void changeIconInTabLayout(int position) {
        mBottomSheetTabLayout.getTabAt(ViewPagerAdapter.POSITION_BUS).setIcon(R.drawable.ic_front_bus_gray);
        mBottomSheetTabLayout.getTabAt(ViewPagerAdapter.POSITION_TRAM).setIcon(R.drawable.ic_tram_public_gray);
        mBottomSheetTabLayout.getTabAt(ViewPagerAdapter.POSITION_PARKING).setIcon(R.drawable.ic_parking_gray);
        switch (position) {
            case ViewPagerAdapter.POSITION_BUS:
                mBottomSheetTabLayout.getTabAt(ViewPagerAdapter.POSITION_BUS).setIcon(R.drawable.ic_front_bus_blue);
                break;
            case ViewPagerAdapter.POSITION_TRAM:
                mBottomSheetTabLayout.getTabAt(ViewPagerAdapter.POSITION_TRAM).setIcon(R.drawable.ic_tram_public_blue);
                break;
            case ViewPagerAdapter.POSITION_PARKING:
                mBottomSheetTabLayout.getTabAt(ViewPagerAdapter.POSITION_PARKING).setIcon(R.drawable.ic_parking_blue);
                break;
        }
    }

    private void setDefaultCameraPosition() {
        mMap.setOnMapLoadedCallback(() -> {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(new LatLngBounds
                    (Const.DefCamPos.FIRST_POINTS, Const.DefCamPos.SECOND_POINTS), Const.DefCamPos.MARGE);
            mMap.animateCamera(cameraUpdate);
        });
    }

    private void setMyLocationButton() {
        UiSettings settings = mMap.getUiSettings();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSIONS);
            return;
        }
        mMap.setMyLocationEnabled(true);
        settings.setMyLocationButtonEnabled(true);
    }

}
