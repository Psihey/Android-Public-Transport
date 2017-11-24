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
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.maps.android.clustering.ClusterManager;
import com.orhanobut.logger.Logger;
import com.provectus.public_transport.R;
import com.provectus.public_transport.adapter.StopDetailSectionAdapter;
import com.provectus.public_transport.adapter.TramsAndTrolleyAdapter;
import com.provectus.public_transport.adapter.TransportAndParkingViewPagerAdapter;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.model.ParkingEntity;
import com.provectus.public_transport.model.StopDetailEntity;
import com.provectus.public_transport.model.StopEntity;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.VehicleMarkerInfoModel;
import com.provectus.public_transport.model.VehiclesModel;
import com.provectus.public_transport.model.converter.TransportType;
import com.provectus.public_transport.persistence.database.DatabaseHelper;
import com.provectus.public_transport.utils.Const;
import com.provectus.public_transport.utils.CustomClusterRenderer;
import com.provectus.public_transport.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.os.Looper.getMainLooper;


public class MapsFragmentImpl extends Fragment
        implements MapsFragment, GoogleMap.OnMapClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener,TabLayout.OnTabSelectedListener {

    public static final String TAG_MAP_FRAGMENT = "fragment_map";
    private static final int REQUEST_LOCATION_PERMISSIONS = 1;
    private static final int VIEW_PAGER_PAGE_IN_MEMORY = 3;
    private static final int POLYLINE_WIDTH = 5;
    private static final String VEHICLE_TYPE = "vehicle";
    private static final String ARROW_TYPE = "arrow";
    private static final String TIME_FORMAT_OFFLINE_MODE = "%tT";
    private static final int INTERNET_ERROR_OFFLINE_MODE = 1;
    private static final int SERVER_ERROR_OFFLINE_MODE = 2;
    private static final int PEEK_HEIGHT_INFO_BOTTOM_SHEET = 0;
    private static final int STOP_SERVER_ID_NOT_CHOSEN = -1;
    private static final int LIMIT_POINT_FOR_SMALL_ROUTE = 255;
    private static final int STEP_POINT_FOR_SMALL_ROUTE = 5;
    private static final int STEP_POINT_FOR_BIG_ROUTE = 10;
    private static final String LOCATION_BUTTON_POSITION = "2";
    private static final String COMPASS_BUTTON_POSITION = "5";
    private static final int PARKING_TAB_POSITION = 2;
    private static final int NOT_PARKING_TAB_POSITION = -1;

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
    @BindView(R.id.relative_layout_container_route_detail)
    RelativeLayout mRelativeRouteContainer;
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
    ImageView mImageButtonFavouriteInfo;
    @BindView(R.id.tv_route_info_transport_fee_value)
    TextView mTextViewRouteInfoFee;
    @BindView(R.id.tv_route_info_transport_route_distance_value)
    TextView mTextViewRouteInfoDistance;
    @BindView(R.id.tv_first_stop_route_detail)
    TextView mTextViewFirstStopRouteDetail;
    @BindView(R.id.tv_last_stop_route_detail)
    TextView mTextViewLastStopRouteDetail;


    @BindView(R.id.relative_container_stop_detail)
    RelativeLayout mRelativeContainerStopDetail;
    @BindView(R.id.recycler_view_stop_detail)
    RecyclerView mRecyclerViewStopDetail;
    @BindView(R.id.tv_name_stop_stop_detail)
    TextView mTextViewStopName;

    @BindView(R.id.relative_container_parking_detail)
    RelativeLayout mRelativeLayoutParkingDetail;
    @BindView(R.id.tv_parking_name)
    TextView mTextViewParkingName;
    @BindView(R.id.tv_parking_place)
    TextView mTextViewParkingPlace;
    @BindView(R.id.tv_parking_type)
    TextView mTextViewParkingType;

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
    private BottomSheetBehavior mBottomSheetStopDetail;
    private BottomSheetBehavior mBottomSheetParkingDetail;
    private long mCurrentVehiclesId;
    private String mLastOnlineTime;
    private TransportEntity mCurrentTransportInfo;
    private Handler mHandler = new Handler(getMainLooper());
    private Map<Integer, List<StopEntity>> mCurrentStopEntityOnMap = new ConcurrentHashMap<>();
    private String mChosenStopName;
    private ClusterManager<ParkingEntity> mClusterManager;
    private View rootView;
    private TramsAndTrolleyAdapter mTramsAdapter;
    private TramsAndTrolleyAdapter mTrolleybusAdapter;
    private int mCurrentTabSelected;
    private MenuItem mMenuItemSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initViewPager();
        mColorRouteList = this.getResources().getIntArray(R.array.route_color_array);
        if (mMapsPresenter == null) {
            mMapsPresenter = new MapsFragmentPresenterImpl();
        }
        mMapsPresenter.bindView(this);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBottomSheetVehicleInfo = BottomSheetBehavior.from(mConstraintVehicleContainer);
        mBottomSheetVehicleInfo.setPeekHeight(PEEK_HEIGHT_INFO_BOTTOM_SHEET);
        mBottomSheetVehicleInfo.setHideable(true);
        mBottomSheetRouteInfo = BottomSheetBehavior.from(mRelativeRouteContainer);
        mBottomSheetRouteInfo.setPeekHeight(PEEK_HEIGHT_INFO_BOTTOM_SHEET);
        mBottomSheetRouteInfo.setHideable(true);
        mBottomSheetStopDetail = BottomSheetBehavior.from(mRelativeContainerStopDetail);
        mBottomSheetStopDetail.setPeekHeight(PEEK_HEIGHT_INFO_BOTTOM_SHEET);
        mBottomSheetStopDetail.setHideable(true);
        mBottomSheetParkingDetail = BottomSheetBehavior.from(mRelativeLayoutParkingDetail);
        mBottomSheetParkingDetail.setPeekHeight(PEEK_HEIGHT_INFO_BOTTOM_SHEET);
        mBottomSheetParkingDetail.setHideable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        mMapsPresenter.unbindView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getTramsAndTrolleybusAdapter(BusEvents.SendTramsAndTrolleyAdapter adapter) {
        if (adapter.getType() == Const.TransportType.TRAMS_ADAPTER) {
            mTramsAdapter = adapter.getTramsAndTrolleyAdapter();
        } else if (adapter.getType() == Const.TransportType.TROLLEYBUSES_ADAPTER) {
            mTrolleybusAdapter = adapter.getTramsAndTrolleyAdapter();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        mMenuItemSearch = menu.findItem(R.id.search);

        MenuItemCompat.setOnActionExpandListener(mMenuItemSearch, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mViewPagerBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_EXPANDED);
                if (mCurrentTabSelected == PARKING_TAB_POSITION) {
                    mBottomSheetTabLayout.getTabAt(TransportAndParkingViewPagerAdapter.POSITION_BUS).select();
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mViewPagerBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_COLLAPSED);
                return true;
            }
        });
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mMenuItemSearch);
        SearchView.SearchAutoComplete searchText = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchText.setHintTextColor(Color.GRAY);
        searchText.setTextColor(Color.BLACK);
        ImageView searchIconClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchIconClose.setImageResource(R.drawable.ic_close_gray_24dp);
        searchRoute(searchView);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setOnClickListener(v -> mMenuItemSearch.expandActionView());
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == PARKING_TAB_POSITION) {
            if (mMenuItemSearch.isActionViewExpanded()) {
                mMenuItemSearch.collapseActionView();
            }
            if (checkOnReadyMap()){
                mMap.clear();
            }
            mCurrentTabSelected = PARKING_TAB_POSITION;
            EventBus.getDefault().post(new BusEvents.UnselectedAllItems());
            mViewPagerBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_COLLAPSED);
            mMapsPresenter.stopGetVehicles();
            mMapsPresenter.getAllParking();
        } else {
            mCurrentTabSelected = NOT_PARKING_TAB_POSITION;
            mViewPagerBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_EXPANDED);
        }
        mBottomSheetRouteInfo.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetVehicleInfo.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetStopDetail.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (tab.getPosition() == PARKING_TAB_POSITION) {
            if (checkOnReadyMap()){
                mMap.clear();
            }
            mClusterManager.clearItems();
            mBottomSheetParkingDetail.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab.getPosition() == PARKING_TAB_POSITION) {
            mViewPagerBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_COLLAPSED);
        } else
            mViewPagerBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mIsMapReady = true;
        mMap = googleMap;
        mClusterManager = new ClusterManager<>(getActivity(), mMap);
        setDefaultCameraPosition();
        setMyLocationButton();
        moveCompassButton();
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnCameraIdleListener(mClusterManager);
        final CustomClusterRenderer renderer = new CustomClusterRenderer(getActivity(), mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mBottomSheetVehicleInfo.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetRouteInfo.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetStopDetail.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetParkingDetail.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() != null) {
            if (marker.getTag() instanceof VehiclesModel) {
                mBottomSheetVehicleInfo.setState(BottomSheetBehavior.STATE_EXPANDED);
                openVehicleInfo(marker);
            } else if (marker.getTag() instanceof StopEntity) {
                mBottomSheetStopDetail.setState(BottomSheetBehavior.STATE_EXPANDED);
                openStopInfo(marker);
            } else if (marker.getTag() instanceof ParkingEntity) {
                mBottomSheetParkingDetail.setState(BottomSheetBehavior.STATE_EXPANDED);
                openParkingInfo(marker);
            }
        }
        return false;
    }

    @Override
    public void showErrorSnackbar(int message) {
        if (message == R.string.snack_bar_no_vehicles_no_internet_connection) {
            setOfflineMode(INTERNET_ERROR_OFFLINE_MODE);
        } else if (message == R.string.snack_bar_no_vehicles_server_not_response) {
            setOfflineMode(SERVER_ERROR_OFFLINE_MODE);
        } else {
            Snackbar snackbar = Snackbar.make(mContainerLayout, message, Snackbar.LENGTH_LONG);
            snackbar.show();
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
                marker.setTag(vehiclesModel);
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
    public void drawStops(List<StopEntity> stopping) {
        BitmapDescriptor stopIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_temp_stop);
        List<Marker> allStops = new ArrayList<>();
        for (StopEntity stopEntity : stopping) {
            double lat = stopEntity.getLatitude();
            double lng = stopEntity.getLongitude();
            LatLng latLng = new LatLng(lat, lng);
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
            marker.setTag(stopEntity);
            marker.setIcon(stopIcon);
            allStops.add(marker);
        }
        mAllCurrentStopsOnMap.put(mTransportNumber, allStops);
        mCurrentStopEntityOnMap.put(mTransportNumber, stopping);
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

        for (Map.Entry<Integer, List<StopEntity>> entry : mCurrentStopEntityOnMap.entrySet()) {
            Integer key = entry.getKey();
            if (key == mTransportNumber) {
                mCurrentStopEntityOnMap.remove(key);
            }
        }
    }

    @Override
    public void getInfoTransport(int transportNumber, long transportId) {
        mTransportNumber = transportNumber;
        mTransportId = transportId;
        mHandler.post(() -> mBottomSheetRouteInfo.setState(BottomSheetBehavior.STATE_HIDDEN));
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
            if (routes.getPoints().size() > LIMIT_POINT_FOR_SMALL_ROUTE) {
                if (i % STEP_POINT_FOR_SMALL_ROUTE == 0) {
                    currentArrowDirection.add(drawArrowsRoute(previousLatLng, currentLatLng));
                }
            } else {
                if (i % STEP_POINT_FOR_BIG_ROUTE == 0) {
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
    public void sendVehicleInfo(VehicleMarkerInfoModel vehicleMarkerInfoModel) {
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
    public void routeNotSelected() {
        mBottomSheetVehicleInfo.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetStopDetail.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void openRouteInfo(TransportEntity transportEntity) {
        mCurrentTransportInfo = transportEntity;
        String transportType;
        mTextViewFirstStopRouteDetail.setText(mCurrentTransportInfo.getFirstStop());
        mTextViewLastStopRouteDetail.setText(mCurrentTransportInfo.getLastStop());
        mBottomSheetRouteInfo.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (transportEntity.getType().equals(TransportType.TRAM_TYPE)) {
            transportType = getString(R.string.transport_type_tram);
            mImageViewRouteInfoIcon.setImageResource(R.drawable.ic_tram_gray_24_dp);
        } else {
            transportType = getString(R.string.transport_type_trolleybus);
            mImageViewRouteInfoIcon.setImageResource(R.drawable.ic_trolley_gray_24_dp);
        }
        mTextViewRouteInfoNumber.setText(getResources().getString(R.string.text_view_route_number, transportEntity.getNumber()));
        mTextViewRouteInfoDistance.setText(getResources().getString(R.string.text_view_transport_route_distance, String.valueOf(transportEntity.getDistance())));
        mTextViewRouteInfoType.setText(transportType);
        if (transportEntity.isFavourites()) {
            mImageButtonFavouriteInfo.setImageResource(R.drawable.ic_favorite_blue_24_dp);
        } else {
            mImageButtonFavouriteInfo.setImageResource(R.drawable.ic_favorite_gray_24_dp);
        }
        mTextViewRouteInfoFee.setText(getResources().getString(R.string.text_view_transport_fee, String.valueOf(transportEntity.getCost())));
    }

    @Override
    public void addAllParkingToCluster(List<ParkingEntity> parkingEntities) {
        for (ParkingEntity parkingEntity : parkingEntities) {
            mClusterManager.addItem(parkingEntity);
        }
        mClusterManager.cluster();
    }

    @OnClick(R.id.ib_route_info_favorite_icon)
    public void setFavourites() {
        if (mCurrentTransportInfo.isFavourites()) {
            mCurrentTransportInfo.setIsFavourites(false);
            updateFavourites();
            mImageButtonFavouriteInfo.setImageResource(R.drawable.ic_favorite_gray_24_dp);
            return;
        }
        mCurrentTransportInfo.setIsFavourites(true);
        updateFavourites();
        mImageButtonFavouriteInfo.setImageResource(R.drawable.ic_favorite_blue_24_dp);
    }

    private void updateFavourites() {
        Completable.defer(() -> Completable.fromCallable(this::updateFavouritesDB))
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        () -> {
                        },
                        throwable -> Logger.d(throwable.getMessage())
                );
    }

    private void searchRoute(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mTramsAdapter !=null && mTrolleybusAdapter !=null){
                    mTramsAdapter.getFilter().filter(newText);
                    mTrolleybusAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    private boolean updateFavouritesDB() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().updateFavourites(mCurrentTransportInfo);
        return true;
    }

    private void setOfflineMode(int code) {
        mHandler.post(() -> {
            mTextViewOfflineMode.setVisibility(View.VISIBLE);
            if (code == INTERNET_ERROR_OFFLINE_MODE) {
                if (mLastOnlineTime == null) {
                    mTextViewOfflineMode.setText(R.string.snack_bar_no_vehicles_no_internet_connection);
                    return;
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
        mBottomSheetTabLayout.addOnTabSelectedListener(this);
        BottomSheetUtils.setupViewPager(mViewPagerTransportAndParking);
        mViewPagerBottomSheetBehavior = ViewPagerBottomSheetBehavior.from(mRelativeLayoutBottomSheet);
        mViewPagerBottomSheetBehavior.setBottomSheetCallback(new ViewPagerBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (mCurrentTabSelected == PARKING_TAB_POSITION && mViewPagerBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_DRAGGING) {
                    mViewPagerBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                mBottomSheetVehicleInfo.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mBottomSheetRouteInfo.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mBottomSheetStopDetail.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                //ignore
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
        moveLocationButtonToBottom();
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

    private void moveLocationButtonToBottom() {
        View locationButton = ((View) rootView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt(LOCATION_BUTTON_POSITION));
        RelativeLayout.LayoutParams locationButtonLayoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        locationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        locationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        locationButtonLayoutParams.setMargins(0, 0, 30, 150);
    }

    private void moveCompassButton() {
        View compassButton = ((View) rootView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt(COMPASS_BUTTON_POSITION));
        RelativeLayout.LayoutParams compassButtonLayoutParams = (RelativeLayout.LayoutParams) compassButton.getLayoutParams();
        compassButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        compassButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        compassButtonLayoutParams.setMargins(0, 0, 30, 150);
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

    private void openStopInfo(Marker marker) {
        StopEntity currentStopEntity = (StopEntity) marker.getTag();
        long stopServerId = STOP_SERVER_ID_NOT_CHOSEN;
        for (Map.Entry<Integer, List<StopEntity>> entry : mCurrentStopEntityOnMap.entrySet()) {
            List<StopEntity> currentMarkers = entry.getValue();
            for (StopEntity currentMarker : currentMarkers) {
                if (currentStopEntity.getServerId() == currentMarker.getServerId()) {
                    mChosenStopName = currentMarker.getTitle();
                    stopServerId = currentMarker.getServerId();
                }
            }
        }
        if (stopServerId != STOP_SERVER_ID_NOT_CHOSEN) {
            DatabaseHelper.getPublicTransportDatabase().stopDetailDao().getStopDetail(stopServerId)
                    .map(list -> {
                        Collections.sort(list, sortByNumber);
                        return list;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Logger.d(throwable.getMessage()))
                    .subscribe(this::getStopDetail);
        }
    }

    private void getStopDetail(List<StopDetailEntity> stopDetailEntity) {
        if (stopDetailEntity != null && !stopDetailEntity.isEmpty()) {
            if (mBottomSheetVehicleInfo.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheetVehicleInfo.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            List<StopDetailEntity> tramList = new ArrayList<>();
            List<StopDetailEntity> trolleybusList = new ArrayList<>();

            for (StopDetailEntity currentStop : stopDetailEntity) {
                if (currentStop.getTransportType().equals(TransportType.TRAM_TYPE)) {
                    tramList.add(currentStop);
                } else {
                    trolleybusList.add(currentStop);
                }
            }

            SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();

            StopDetailSectionAdapter tramSection = new StopDetailSectionAdapter(getContext(), tramList, getString(R.string.transport_type_tram));
            StopDetailSectionAdapter trolleybusSection = new StopDetailSectionAdapter(getContext(), trolleybusList, getString(R.string.transport_type_trolleybus));

            sectionAdapter.addSection(tramSection);
            sectionAdapter.addSection(trolleybusSection);
            mRecyclerViewStopDetail.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerViewStopDetail.setAdapter(sectionAdapter);
            mTextViewStopName.setText(mChosenStopName);
        } else {
            showErrorSnackbar(R.string.snack_bar_no_stop_detail);
        }

    }

    private void openParkingInfo(Marker marker) {
        ParkingEntity parkingEntity = (ParkingEntity) marker.getTag();
        if (parkingEntity != null) {
            String parkingType = null;
            switch (parkingEntity.getType()) {
                case ALLDAY_TYPE:
                    parkingType = getString(R.string.maps_fragment_round_the_clock_parking);
                    break;
                case OFFICIAL_TYPE:
                    parkingType = getString(R.string.maps_fragment_official_parking);
                    break;
                case SEASON_TYPE:
                    parkingType = getString(R.string.maps_fragment_season_parking);
                    break;
                case UNDEFINED_TYPE:
                    parkingType = getString(R.string.maps_fragment_daytime_parking);
                    break;
            }
            mTextViewParkingName.setText(parkingEntity.getAddress());
            mTextViewParkingPlace.setText(getResources().getString(R.string.text_view_parking_number_of_places, parkingEntity.getPlaces()));
            mTextViewParkingType.setText(getResources().getString(R.string.text_view_parking_parking_type, parkingType));
        }
    }

    private void openVehicleInfo(Marker marker) {
        String transportType;
        VehiclesModel currentVehiclesModel = (VehiclesModel) marker.getTag();
        for (VehiclesModel vehiclesModel : mAllVehicles) {
            if (currentVehiclesModel.getVehicleId() == vehiclesModel.getVehicleId()) {
                mCurrentVehiclesId = vehiclesModel.getVehicleId();
                if (vehiclesModel.getType().equals(TransportType.TRAM_TYPE)) {
                    transportType = getString(R.string.transport_type_tram);
                    mImageViewTransportIcon.setImageResource(R.drawable.ic_tram_gray_24_dp);
                } else {
                    transportType = getString(R.string.transport_type_trolleybus);
                    mImageViewTransportIcon.setImageResource(R.drawable.ic_trolley_gray_24_dp);
                }
                mTextViewTransportType.setText(transportType);
                mTextViewTransportFee.setText(getResources().getString(R.string.text_view_transport_fee, String.valueOf(vehiclesModel.getCost())));
                mTextViewTransportCapacity.setText(String.valueOf(vehiclesModel.getSeats()));
                mTextViewSpeed.setText(getResources().getString(R.string.text_view_transport_speed, vehiclesModel.getSpeed()));
                mTextViewInventoryNumber.setText(String.valueOf(vehiclesModel.getInventoryNumber()));
                for (Map.Entry<Long, VehicleMarkerInfoModel> entry : mAllCurrentVehicleInfo.entrySet()) {
                    VehicleMarkerInfoModel model = entry.getValue();
                    if (vehiclesModel.getRouteId() == model.getServerId()) {
                        mTextViewRouteDistance.setText(getResources().getString(R.string.text_view_transport_route_distance, String.valueOf(model.getDistance())));
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

    private Comparator<StopDetailEntity> sortByNumber = (t1, t2) -> t1.getNumber() > t2.getNumber() ? 1 : -1;

}