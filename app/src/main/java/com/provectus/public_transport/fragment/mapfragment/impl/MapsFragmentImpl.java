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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.provectus.public_transport.R;
import com.provectus.public_transport.adapter.TransportAndParkingViewPagerAdapter;
import com.provectus.public_transport.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.utils.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import biz.laenger.android.vpbs.BottomSheetUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.provectus.public_transport.utils.Utils.getRandomColor;


public class MapsFragmentImpl extends Fragment implements MapsFragment, OnMapReadyCallback {

    public static final String TAG_MAP_FRAGMENT = "fragment_map";
    private static final int REQUEST_LOCATION_PERMISSIONS = 1;
    private static final int VIEW_PAGER_PAGE_IN_MEMORY = 3;
    private static final int POLYLINE_WIDTH =5;

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
    private Map<Integer, Polyline> mAllCurrentRoutesOnMap = new ConcurrentHashMap<>();
    private Map<Integer, List<Marker>> mAllCurrentMarkerOnMap = new ConcurrentHashMap<>();
    private boolean mIsSelectRoute;
    private int mTransportNumber;

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
    public void onMapReady(GoogleMap googleMap) {
        mIsMapReady = true;
        mMap = googleMap;
        setDefaultCameraPosition();
        setMyLocationButton();
    }

    @Override
    public void drawSelectedPosition(PolylineOptions sortedRoutes, List<MarkerOptions> stopping, int transportNumber, boolean isChecked) {
        mIsSelectRoute = isChecked;
        mTransportNumber = transportNumber;
        if (checkOnReadyMap()) drawRoutesWithStopOnMap(sortedRoutes, stopping);
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

    private boolean checkOnReadyMap() {
        return !(!mIsMapReady || mMap == null);
    }

    private void drawRoutesWithStopOnMap(PolylineOptions listDirection, List<MarkerOptions> stopping) {
        List<Marker> currentMarkers = new ArrayList<>();
        if (mIsSelectRoute) {
            for (MarkerOptions markerOptions : stopping) {
                Marker marker = mMap.addMarker(markerOptions);
                marker.setIcon(mStopIcon);
                currentMarkers.add(marker);
            }

            Polyline polyline = mMap.addPolyline(listDirection);
            polyline.setColor(getRandomColor());
            polyline.setWidth(POLYLINE_WIDTH);

            mAllCurrentRoutesOnMap.put(mTransportNumber, polyline);
            mAllCurrentMarkerOnMap.put(mTransportNumber, currentMarkers);
        } else {
            for (Map.Entry<Integer, Polyline> entry : mAllCurrentRoutesOnMap.entrySet()) {
                Integer key = entry.getKey();
                Polyline value = entry.getValue();
                if (key == mTransportNumber) {
                    value.remove();
                    mAllCurrentRoutesOnMap.remove(mTransportNumber);
                }
            }
            for (Map.Entry<Integer, List<Marker>> entry : mAllCurrentMarkerOnMap.entrySet()) {
                Integer key = entry.getKey();
                List<Marker> list = entry.getValue();
                if (key == mTransportNumber) {
                    for (Marker marker : list) {
                        marker.remove();
                    }
                    mAllCurrentMarkerOnMap.remove(mTransportNumber);
                }
            }
        }
    }

    private void initViewPager() {
        TransportAndParkingViewPagerAdapter mPagerAdapter = new TransportAndParkingViewPagerAdapter(getFragmentManager());
        mViewPagerTransportAndParking.setOffscreenPageLimit(VIEW_PAGER_PAGE_IN_MEMORY);
        mViewPagerTransportAndParking.setAdapter(mPagerAdapter);
        mBottomSheetTabLayout.setupWithViewPager(mViewPagerTransportAndParking);
        mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_BUS).setIcon(R.drawable.trolleybus_tab_drawable_state);
        mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_TRAM).setIcon(R.drawable.tram_tab_drawable_state);
        mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_PARKING).setIcon(R.drawable.parking_tab_drawable_state);
        mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_FAVOURITES).setIcon(R.drawable.favourites_tab_drawable_state);
        BottomSheetUtils.setupViewPager(mViewPagerTransportAndParking);
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
