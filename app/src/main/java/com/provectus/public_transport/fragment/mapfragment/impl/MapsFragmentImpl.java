package com.provectus.public_transport.fragment.mapfragment.impl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.provectus.public_transport.R;
import com.provectus.public_transport.adapter.TransportAndParkingViewPagerAdapter;
import com.provectus.public_transport.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.model.VehiclesModel;
import com.provectus.public_transport.utils.Const;
import com.provectus.public_transport.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import biz.laenger.android.vpbs.BottomSheetUtils;
import biz.laenger.android.vpbs.ViewPagerBottomSheetBehavior;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MapsFragmentImpl extends Fragment implements MapsFragment, OnMapReadyCallback {

    public static final String TAG_MAP_FRAGMENT = "fragment_map";
    private static final int REQUEST_LOCATION_PERMISSIONS = 1;
    private static final int VIEW_PAGER_PAGE_IN_MEMORY = 3;
    private static final int POLYLINE_WIDTH = 5;

    @BindView(R.id.bottom_sheet_view_pager)
    ViewPager mViewPagerTransportAndParking;
    @BindView(R.id.bottom_sheet_tab_layout)
    TabLayout mBottomSheetTabLayout;
    @BindView(R.id.coordinator_layout_fragment_container)
    CoordinatorLayout mContainerLayout;
    @BindView(R.id.container_bottom_sheet)
    RelativeLayout mRelativeLayoutBottomSheet;
    private MapsFragmentPresenter mMapsPresenter;
    private Unbinder mUnbinder;
    private GoogleMap mMap;
    private boolean mIsMapReady;
    private Map<Integer, Polyline> mAllCurrentRoutesOnMap = new ConcurrentHashMap<>();
    private Map<Integer, List<Marker>> mAllCurrentStopsOnMap = new ConcurrentHashMap<>();
    private Map<Integer, List<Marker>> mAllCurrentArrowOnMap = new ConcurrentHashMap<>();
    private Map<Long, Integer> mAllCurrentRouteWithColorOnMap = new ConcurrentHashMap<>();
    private List<Marker> mAllVehicles = new ArrayList<>();
    private boolean mIsSelectRoute;
    private int mTransportNumber;
    private long mTransportId;
    private int mIndexColorRoute = -1;
    private int[] mColorRouteList;
    private ViewPagerBottomSheetBehavior mViewPagerBottomSheetBehavior;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initViewPager();
        mColorRouteList = this.getResources().getIntArray(R.array.route_color_array);
        if (mMapsPresenter == null) {
            mMapsPresenter = new MapsFragmentPresenterImpl();
        }
        mMapsPresenter.bindView(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        mMapsPresenter.unregisteredEventBus();
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
    public void showErrorSnackbar(int message) {
        Snackbar snackbar = Snackbar.make(mContainerLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void drawVehicles(List<VehiclesModel> vehiclesModels) {
        removeVehiclesFromMap();
        mAllVehicles.clear();
        if (vehiclesModels != null) {
            for (VehiclesModel vehiclesModel : vehiclesModels) {
                int azimuth = vehiclesModel.getAzimuth();
                double lat = vehiclesModel.getLatitude();
                double lng = vehiclesModel.getLongitude();
                LatLng latLn = new LatLng(lat, lng);
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLn));
                mAllVehicles.add(marker);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(Utils.tintImage(Utils.drawVehicleDirection(this, azimuth, vehiclesModel.getType()), colorForVehicles(mAllCurrentRouteWithColorOnMap, vehiclesModel.getRouteId()))));
                marker.setFlat(true);
            }
        }
    }

    @Override
    public void removeVehiclesFromMap() {
        for (Marker marker : mAllVehicles) {
            marker.remove();
        }
    }

    @Override
    public void drawStops(List<MarkerOptions> stopping) {
        List<Marker> allStops = new ArrayList<>();
        if (mIsSelectRoute) {
            for (MarkerOptions markerOptions : stopping) {
                Marker marker = mMap.addMarker(markerOptions);
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_temp_stop));
                allStops.add(marker);
            }
            mAllCurrentStopsOnMap.put(mTransportNumber, allStops);
        } else {
            for (Map.Entry<Integer, List<Marker>> entry : mAllCurrentStopsOnMap.entrySet()) {
                Integer key = entry.getKey();
                List<Marker> list = entry.getValue();
                if (key == mTransportNumber) {
                    for (Marker marker : list) {
                        marker.remove();
                    }
                    mAllCurrentStopsOnMap.remove(mTransportNumber);
                }
            }
        }
    }

    @Override
    public void getInfoTransport(int transportNumber, boolean isChecked, long transportId) {
        mTransportNumber = transportNumber;
        mIsSelectRoute = isChecked;
        mTransportId = transportId;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 2 && requestCode == REQUEST_LOCATION_PERMISSIONS) {
            setMyLocationButton();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean checkOnReadyMap() {
        return !(!mIsMapReady || mMap == null);
    }

    @Override
    public void drawRoutesWithDirection(PolylineOptions routes) {
        if (mIsSelectRoute) {
            Polyline polyline = mMap.addPolyline(routes);
            polyline.setWidth(POLYLINE_WIDTH);
            mAllCurrentRouteWithColorOnMap.put(mTransportId, mColorRouteList[mIndexColorRoute]);
            polyline.setColor(mColorRouteList[mIndexColorRoute]);

            LatLng previousLatLng = null;
            int i = 0;
            List<Marker> currentArrowDirection = new ArrayList<>();
            Collections.reverse(routes.getPoints());
            for (LatLng currentLatLng : routes.getPoints()) {
                i++;
                if (routes.getPoints().size() > 255) {
                    if (i % 10 == 0) {
                        currentArrowDirection.add(drawArrowsRoute(previousLatLng, currentLatLng));
                    }
                } else {
                    if (i % 5 == 0) {
                        currentArrowDirection.add(drawArrowsRoute(previousLatLng, currentLatLng));
                    }
                }
                previousLatLng = currentLatLng;
            }

            mAllCurrentRoutesOnMap.put(mTransportNumber, polyline);
            mAllCurrentArrowOnMap.put(mTransportNumber, currentArrowDirection);
        } else {
            removeVehiclesFromMap();
            for (Map.Entry<Integer, Polyline> entry : mAllCurrentRoutesOnMap.entrySet()) {
                Integer key = entry.getKey();
                Polyline value = entry.getValue();
                if (key == mTransportNumber) {
                    value.remove();
                    mAllCurrentRoutesOnMap.remove(mTransportNumber);
                }
            }

            for (Map.Entry<Integer, List<Marker>> entry : mAllCurrentArrowOnMap.entrySet()) {
                Integer key = entry.getKey();
                List<Marker> list = entry.getValue();
                if (key == mTransportNumber) {
                    for (Marker marker : list) {
                        marker.remove();
                    }
                    mAllCurrentArrowOnMap.remove(mTransportNumber);
                }
            }

        }
    }

    @Override
    public void getColorForRoute() {
        if (mIndexColorRoute == mColorRouteList.length - 1) {
            mIndexColorRoute = 0;
        } else {
            mIndexColorRoute++;
        }
    }

    private Marker drawArrowsRoute(LatLng previousLatLng, LatLng currentLatLng) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_drirection);
        double rotation = SphericalUtil.computeHeading(previousLatLng, currentLatLng);
        Marker marker = mMap.addMarker(new MarkerOptions().position((previousLatLng)));
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(Utils.tintImage(bitmap, colorForVehicles(mAllCurrentRouteWithColorOnMap, mTransportId))));
        marker.setRotation((float) rotation);
        marker.setFlat(true);
        return marker;
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
        mViewPagerBottomSheetBehavior = ViewPagerBottomSheetBehavior.from(mRelativeLayoutBottomSheet);
        mBottomSheetTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPagerBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPagerBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_EXPANDED);
            }
        });
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

    private int colorForVehicles(Map<Long, Integer> allRoutes, long routeId) {
        int color = 0;
        for (Map.Entry<Long, Integer> entry : allRoutes.entrySet()) {
            Long key = entry.getKey();
            Integer value = entry.getValue();
            if (key == routeId) {
                color = value;
            }
        }
        return color;
    }

}
