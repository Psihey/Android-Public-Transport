package com.provectus.public_transport.fragment.mapfragment.impl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.provectus.public_transport.R;
import com.provectus.public_transport.adapter.TransportAndParkingViewPagerAdapter;
import com.provectus.public_transport.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.utils.Const;

import java.util.List;
import java.util.Map;

import biz.laenger.android.vpbs.BottomSheetUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.provectus.public_transport.utils.Utils.getRandomColor;

/**
 * Created by Evgeniy on 8/10/2017.
 */

public class MapsFragmentImpl extends Fragment implements MapsFragment, OnMapReadyCallback {

    public static final String TAG_MAP_FRAGMENT = "fragment_map";
    private static final int REQUEST_LOCATION_PERMISSIONS = 1;

    @BindView(R.id.bottom_sheet_view_pager)
    ViewPager mViewPagerTransportAndParking;
    @BindView(R.id.bottom_sheet_tab_layout)
    TabLayout mBottomSheetTabLayout;
    @BindView(R.id.coordinator_layout_fragment_container)
    CoordinatorLayout mContainerLayout;

    private MapsFragmentPresenter mMapsPresenter;
    private Unbinder mUnbinder;
    private GoogleMap mMap;
    private boolean mIsMapReady;
    private BitmapDescriptor mStopIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initViewPager();
        mStopIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_temp_stop);
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
        mMapsPresenter.unregisteredEventBus();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        mMapsPresenter.unbindView();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mIsMapReady = true;
        mMap = googleMap;
        setDefaultCameraPosition();
        setMyLocationButton();
    }

    @Override
    public void drawSelectedPosition(Map<Integer, PolylineOptions> sortedRoutes, Map<Integer, List<MarkerOptions>> stopping) {
        if (!mIsMapReady || mMap == null || sortedRoutes == null) {
            return;
        }
        drawRoutesWithStopOnMap(sortedRoutes, stopping);
    }

    @Override
    public void showErrorSnackbar() {
        Snackbar snackbar = Snackbar.make(mContainerLayout, R.string.snack_bar_no_data_for_this_route, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 2 && requestCode == REQUEST_LOCATION_PERMISSIONS) {
            setMyLocationButton();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void drawRoutesWithStopOnMap(Map<Integer, PolylineOptions> listDirection, Map<Integer, List<MarkerOptions>> stopping) {

        mMap.clear();
        for (Map.Entry<Integer, PolylineOptions> entry : listDirection.entrySet()) {
            PolylineOptions value = entry.getValue();
            Polyline polyline = mMap.addPolyline(value);
            polyline.setColor(getRandomColor());
            polyline.setWidth(5);
        }
        for (Map.Entry<Integer, List<MarkerOptions>> entry : stopping.entrySet()) {
            for (MarkerOptions value : entry.getValue()) {
                mMap.addMarker(value).setIcon(mStopIcon);
            }
        }
    }

    private void initViewPager() {
        TransportAndParkingViewPagerAdapter mPagerAdapter = new TransportAndParkingViewPagerAdapter(getFragmentManager());
        mViewPagerTransportAndParking.setOffscreenPageLimit(3);
        mViewPagerTransportAndParking.setAdapter(mPagerAdapter);
        mViewPagerTransportAndParking.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        mBottomSheetTabLayout.setupWithViewPager(mViewPagerTransportAndParking);
        mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_BUS).setIcon(R.drawable.ic_trolley_blue_24_dp);
        mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_TRAM).setIcon(R.drawable.ic_tram_gray_24_dp);
        mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_PARKING).setIcon(R.drawable.ic_local_parking_gray_24_dp);
        mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_FAVOURITES).setIcon(R.drawable.ic_favorite_gray_24_dp);
        BottomSheetUtils.setupViewPager(mViewPagerTransportAndParking);
    }

    private void changeIconInTabLayout(int position) {
        mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_BUS).setIcon(R.drawable.ic_trolley_gray_24_dp);
        mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_TRAM).setIcon(R.drawable.ic_tram_gray_24_dp);
        mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_PARKING).setIcon(R.drawable.ic_local_parking_gray_24_dp);
        mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_FAVOURITES).setIcon(R.drawable.ic_favorite_gray_24_dp);
        switch (position) {
            case TransportAndParkingViewPagerAdapter.POSITION_BUS:
                mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_BUS).setIcon(R.drawable.ic_trolley_blue_24_dp);
                break;
            case TransportAndParkingViewPagerAdapter.POSITION_TRAM:
                mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_TRAM).setIcon(R.drawable.ic_tram_blue_24_dp);
                break;
            case TransportAndParkingViewPagerAdapter.POSITION_PARKING:
                mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_PARKING).setIcon(R.drawable.ic_local_parking_blue_24_dp);
                break;
            case TransportAndParkingViewPagerAdapter.POSITION_FAVOURITES:
                mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_FAVOURITES).setIcon(R.drawable.ic_favorite_blue_24_dp);
                break;
        }
    }

    private void setDefaultCameraPosition() {
        mMap.setOnMapLoadedCallback(() -> {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(new LatLngBounds
                    (Const.DefaultCameraPosition.ODESSA_FIRST_POINTS, Const.DefaultCameraPosition.ODESSA_SECOND_POINTS), Const.DefaultCameraPosition.ZOOM_ON_MAP);
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
