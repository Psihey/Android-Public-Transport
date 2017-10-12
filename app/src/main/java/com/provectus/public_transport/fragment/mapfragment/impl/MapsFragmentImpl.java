package com.provectus.public_transport.fragment.mapfragment.impl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.orhanobut.logger.Logger;
import com.provectus.public_transport.R;
import com.provectus.public_transport.adapter.TransportAndParkingViewPagerAdapter;
import com.provectus.public_transport.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.VehicleMarkerInfoModel;
import com.provectus.public_transport.model.VehiclesModel;
import com.provectus.public_transport.model.converter.TransportType;
import com.provectus.public_transport.persistence.database.DatabaseHelper;
import com.provectus.public_transport.utils.Const;
import com.provectus.public_transport.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import biz.laenger.android.vpbs.BottomSheetUtils;
import biz.laenger.android.vpbs.ViewPagerBottomSheetBehavior;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.os.Looper.getMainLooper;


public class MapsFragmentImpl extends Fragment implements MapsFragment, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final String TAG_MAP_FRAGMENT = "fragment_map";
    private static final int REQUEST_LOCATION_PERMISSIONS = 1;
    private static final int VIEW_PAGER_PAGE_IN_MEMORY = 3;
    private static final int POLYLINE_WIDTH = 5;
    private static final String VEHICLE_TYPE = "vehicle";
    private static final String ARROW_TYPE = "arrow";
    private static final String TIME_FORMAT_OFFLINE_MODE = "%tT";
    private static final int INTERNET_ERROR_OFFLINE_MODE = 1;
    private static final int SERVER_ERROR_OFFLINE_MODE = 2;

    @BindView(R.id.bottom_sheet_view_pager)
    ViewPager mViewPagerTransportAndParking;
    @BindView(R.id.bottom_sheet_tab_layout)
    TabLayout mBottomSheetTabLayout;
    @BindView(R.id.coordinator_layout_fragment_container)
    CoordinatorLayout mContainerLayout;
    @BindView(R.id.container_bottom_sheet)
    RelativeLayout mRelativeLayoutBottomSheet;
    @BindView(R.id.constraint_container_vehicle_info)
    ConstraintLayout mConstraintVehicleContainer;
    @BindView(R.id.constraint_layout_route)
    ConstraintLayout mConstraintRouteContainer;
    @BindView(R.id.tv_route_number)
    TextView mTextViewRouteNumber;
    @BindView(R.id.tv_transport_type_value)
    TextView mTextViewTransportType;
    @BindView(R.id.tv_transport_speed_value)
    TextView mTextViewSpeed;
    @BindView(R.id.tv_transport_inventory_number_value)
    TextView mTextViewInventoryNumber;
    @BindView(R.id.tv_transport_capacity_value)
    TextView mTextViewTransportCapacity;
    @BindView(R.id.tv_transport_fee_value)
    TextView mTextViewTransportFee;
    @BindView(R.id.tv_transport_route_distance_value)
    TextView mTextViewRouteDistance;
    @BindView(R.id.iv_transport_icon)
    ImageView mImageViewTransportIcon;
    @BindView(R.id.tv_offline_mode)
    TextView mTextViewOfflineMode;

    @BindView(R.id.tv_route_info_number)
    TextView mTextViewRouteInfoNumber;
    @BindView(R.id.iv_route_info_transport_icon)
    ImageView mImageViewRouteInfoIcon;
    @BindView(R.id.tv_transport_route_info_type_value)
    TextView mTextViewRouteInfoType;
    @BindView(R.id.ib_route_info_favorite_icon)
    ImageButton mImageButtonFavouriteInfo;
    @BindView(R.id.tv_route_info_transport_fee_value)
    TextView mTextViewRouteInfoFee;
    @BindView(R.id.tv_route_info_transport_route_distance_value)
    TextView mTextViewRouteInfoDistance;

    private MapsFragmentPresenter mMapsPresenter;
    private Unbinder mUnbinder;
    private GoogleMap mMap;
    private boolean mIsMapReady;
    private Map<Integer, Polyline> mAllCurrentRoutesOnMap = new ConcurrentHashMap<>();
    private Map<Integer, List<Marker>> mAllCurrentStopsOnMap = new ConcurrentHashMap<>();
    private Map<Integer, List<Marker>> mAllCurrentArrowOnMap = new ConcurrentHashMap<>();
    private Map<Long, Integer> mAllCurrentRouteWithColorOnMap = new ConcurrentHashMap<>();
    private Map<Long, VehicleMarkerInfoModel> mAllCurrentVehicleInfo = new ConcurrentHashMap<>();
    private List<Marker> mAllMarkerVehicles = new ArrayList<>();
    private List<VehiclesModel> mAllVehicles;
    private int mTransportNumber;
    private long mTransportId;
    private int mIndexColorRoute = -1;
    private int[] mColorRouteList;
    private ViewPagerBottomSheetBehavior mViewPagerBottomSheetBehavior;
    private BottomSheetBehavior mBottomSheetVehicleInfo;
    private BottomSheetBehavior mBottomSheetRouteInfo;
    private long mCurrentVehiclesId;
    private String mLastOnlineTime;
    private TransportEntity mCurrentTransportInfo;

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
    public void onResume() {
        super.onResume();
        mBottomSheetVehicleInfo = BottomSheetBehavior.from(mConstraintVehicleContainer);
        mBottomSheetVehicleInfo.setPeekHeight(0);
        mBottomSheetVehicleInfo.setHideable(true);
        mBottomSheetRouteInfo = BottomSheetBehavior.from(mConstraintRouteContainer);
        mBottomSheetRouteInfo.setPeekHeight(0);
        mBottomSheetRouteInfo.setHideable(true);

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
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void showErrorSnackbar(int message) {
        Snackbar snackbar = Snackbar.make(mContainerLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
        if (message == R.string.snack_bar_no_vehicles_no_internet_connection) {
            setOfflineMode(INTERNET_ERROR_OFFLINE_MODE);
        } else if (message == R.string.snack_bar_no_vehicles_server_not_response) {
            setOfflineMode(SERVER_ERROR_OFFLINE_MODE);
        }
    }

    @Override
    public void drawVehicles(List<VehiclesModel> vehiclesModels) {
        mTextViewOfflineMode.setVisibility(View.GONE);
        mLastOnlineTime = String.format(Locale.US, TIME_FORMAT_OFFLINE_MODE, new Date());
        mAllVehicles = vehiclesModels;
        removeVehiclesFromMap();
        mAllMarkerVehicles.clear();
        changeDynamicallyVehicleSpeedOnView(vehiclesModels);
        if (vehiclesModels != null) {
            for (VehiclesModel vehiclesModel : vehiclesModels) {
                int azimuth = vehiclesModel.getAzimuth();
                double lat = vehiclesModel.getLatitude();
                double lng = vehiclesModel.getLongitude();
                LatLng latLn = new LatLng(lat, lng);
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLn));
                marker.setTag(vehiclesModel.getVehicleId());
                mAllMarkerVehicles.add(marker);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(Utils.tintImage(Utils.drawVehicleDirection(this, azimuth, vehiclesModel.getType()), colorForVehicles(mAllCurrentRouteWithColorOnMap, vehiclesModel.getRouteId(), VEHICLE_TYPE))));
                marker.setFlat(true);
            }
        }
    }

    @Override
    public void removeVehiclesFromMap() {
        for (Marker marker : mAllMarkerVehicles) {
            marker.remove();
        }
    }

    @Override
    public void drawStops(List<MarkerOptions> stopping) {
        BitmapDescriptor stopIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_temp_stop);
        List<Marker> allStops = new ArrayList<>();
        for (MarkerOptions markerOptions : stopping) {
            Marker marker = mMap.addMarker(markerOptions);
            marker.setIcon(stopIcon);
            allStops.add(marker);
        }
        mAllCurrentStopsOnMap.put(mTransportNumber, allStops);
    }

    @Override
    public void removeStopsFromMap() {
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

    @Override
    public void getInfoTransport(int transportNumber, long transportId) {
        mTransportNumber = transportNumber;
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
    }

    @Override
    public void removeRoutesWithDirectionFromMap() {
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

    @Override
    public void openVehicleInfo(VehicleMarkerInfoModel vehicleMarkerInfoModel) {
        if (mAllCurrentVehicleInfo.isEmpty()) {
            mAllCurrentVehicleInfo.put(mTransportId, vehicleMarkerInfoModel);
        } else {
            for (Map.Entry<Long, VehicleMarkerInfoModel> entry : mAllCurrentVehicleInfo.entrySet()) {
                Long key = entry.getKey();
                if (key == mTransportNumber) {
                    mAllCurrentVehicleInfo.remove(mTransportNumber);
                } else {
                    mAllCurrentVehicleInfo.put(mTransportId, vehicleMarkerInfoModel);
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() != null) {
            mBottomSheetVehicleInfo.setState(BottomSheetBehavior.STATE_EXPANDED);
            setDataIntoInfoView(marker);
        }
        return false;
    }

    @Override
    public void routeNotSelected() {
        mBottomSheetVehicleInfo.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void openRouteInfo(TransportEntity transportEntity) {
        mCurrentTransportInfo = transportEntity;
        String transportType;
        mBottomSheetRouteInfo.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (transportEntity.getType().equals(TransportType.TRAM_TYPE)) {
            transportType = getString(R.string.transport_type_tram);
            mImageViewRouteInfoIcon.setImageResource(R.drawable.ic_tram_gray_24_dp);
        } else {
            transportType = getString(R.string.transport_type_trolleybus);
            mImageViewRouteInfoIcon.setImageResource(R.drawable.ic_trolley_gray_24_dp);
        }
        mTextViewRouteInfoNumber.setText(getResources().getString(R.string.text_view_route_number, transportEntity.getNumber()));
        mTextViewRouteInfoDistance.setText(getResources().getString(R.string.text_view_transport_route_distance, Double.toString(transportEntity.getDistance())));
        mTextViewRouteInfoType.setText(transportType);
        if (transportEntity.isFavourites()){
            mImageButtonFavouriteInfo.setImageResource(R.drawable.ic_favorite_blue_24_dp);
        }else {
            mImageButtonFavouriteInfo.setImageResource(R.drawable.ic_favorite_gray_24_dp);
        }
    }

    @OnClick(R.id.ib_route_info_favorite_icon)
    public void setFavourites() {
        Logger.d(mCurrentTransportInfo);
        if (mCurrentTransportInfo.isFavourites()) {
            mCurrentTransportInfo.setIsFavourites(false);
            new Thread(() -> DatabaseHelper.getPublicTransportDatabase().transportDao().updateFavourites(mCurrentTransportInfo)).start();
            mImageButtonFavouriteInfo.setImageResource(R.drawable.ic_favorite_gray_24_dp);
            return;
        }
        mCurrentTransportInfo.setIsFavourites(true);
        new Thread(() -> DatabaseHelper.getPublicTransportDatabase().transportDao().updateFavourites(mCurrentTransportInfo)).start();
        mImageButtonFavouriteInfo.setImageResource(R.drawable.ic_favorite_blue_24_dp);
    }

    private void setOfflineMode(int code) {
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(() -> {
            mTextViewOfflineMode.setVisibility(View.VISIBLE);
            if (code == 1) {
                if (mLastOnlineTime == null) {
                    mTextViewOfflineMode.setText(R.string.snack_bar_no_vehicles_no_internet_connection);
                } else {
                    mTextViewOfflineMode.setText(getResources().getString(R.string.text_view_offline_mode, mLastOnlineTime));
                    return;
                }
            }
            mTextViewOfflineMode.setText(R.string.text_view_server_error);

        });

    }

    private Marker drawArrowsRoute(LatLng previousLatLng, LatLng currentLatLng) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_drirection);
        double rotation = SphericalUtil.computeHeading(previousLatLng, currentLatLng);
        Marker marker = mMap.addMarker(new MarkerOptions().position((previousLatLng)));
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(Utils.tintImage(bitmap, colorForVehicles(mAllCurrentRouteWithColorOnMap, mTransportId, ARROW_TYPE))));
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

    private int colorForVehicles(Map<Long, Integer> allRoutes, long routeId, String type) {
        int color = 0;
        for (Map.Entry<Long, Integer> entry : allRoutes.entrySet()) {
            Long key = entry.getKey();
            Integer value = entry.getValue();
            if (key == routeId) {
                color = value;
                if (type.equals(VEHICLE_TYPE)) {
                    float[] hsv = new float[3];
                    Color.colorToHSV(color, hsv);
                    hsv[2] *= 0.7f;
                    color = Color.HSVToColor(hsv);
                }
            }
        }
        return color;
    }

    private void setDataIntoInfoView(Marker marker) {
        String transportType;
        for (VehiclesModel vehiclesModel : mAllVehicles) {
            if (marker.getTag().equals(vehiclesModel.getVehicleId())) {
                mCurrentVehiclesId = vehiclesModel.getVehicleId();
                if (vehiclesModel.getType().equals(TransportType.TRAM_TYPE)) {
                    transportType = getString(R.string.transport_type_tram);
                    mImageViewTransportIcon.setImageResource(R.drawable.ic_tram_gray_24_dp);
                } else {
                    transportType = getString(R.string.transport_type_trolleybus);
                    mImageViewTransportIcon.setImageResource(R.drawable.ic_trolley_gray_24_dp);
                }
                mTextViewTransportType.setText(transportType);
                mTextViewTransportFee.setText(getResources().getString(R.string.text_view_transport_fee, Double.toString(vehiclesModel.getCost())));
                mTextViewTransportCapacity.setText(String.valueOf(vehiclesModel.getSeats()));
                mTextViewSpeed.setText(getResources().getString(R.string.text_view_transport_speed, vehiclesModel.getSpeed()));
                mTextViewInventoryNumber.setText(String.valueOf(vehiclesModel.getInventoryNumber()));
                for (Map.Entry<Long, VehicleMarkerInfoModel> entry : mAllCurrentVehicleInfo.entrySet()) {
                    VehicleMarkerInfoModel model = entry.getValue();
                    if (vehiclesModel.getRouteId() == model.getServerId()) {
                        mTextViewRouteDistance.setText(getResources().getString(R.string.text_view_transport_route_distance, Double.toString(model.getDistance())));
                        mTextViewRouteNumber.setText(getResources().getString(R.string.text_view_route_number, model.getNumber()));
                    }
                }
            }

        }
    }

    private void changeDynamicallyVehicleSpeedOnView(List<VehiclesModel> vehiclesModels) {
        if (mCurrentVehiclesId != 0) {
            for (VehiclesModel vehicleModel : vehiclesModels) {
                if (vehicleModel.getVehicleId() == mCurrentVehiclesId) {
                    mTextViewSpeed.setText(getResources().getString(R.string.text_view_transport_speed, vehicleModel.getSpeed()));
                }
            }
        }
    }
}
