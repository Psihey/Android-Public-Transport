package com.provectus.public_transport.utils;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.provectus.public_transport.R;
import com.provectus.public_transport.model.ParkingEntity;

public class CustomClusterRenderer extends DefaultClusterRenderer<ParkingEntity> {

    private final BitmapDescriptor mMarkerBlue = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pin_blue);
    private final BitmapDescriptor mMarkerRed = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pin_red);
    private final BitmapDescriptor mMarkerYellow = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pin_orange);
    private final BitmapDescriptor mMarkerGreen = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pin_green);

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<ParkingEntity> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onClusterItemRendered(ParkingEntity clusterItem, Marker marker) {
        marker.setTag(clusterItem);
    }

    @Override
    protected void onBeforeClusterItemRendered(ParkingEntity item, MarkerOptions markerOptions) {
        switch (item.getType()) {
            case OFFICIAL_TYPE:
                markerOptions.icon(mMarkerYellow).snippet(item.getSnippet());
                break;
            case ALLDAY_TYPE:
                markerOptions.icon(mMarkerBlue).snippet(item.getSnippet());
                break;
            case SEASON_TYPE:
                markerOptions.icon(mMarkerRed).snippet(item.getSnippet());
                break;
            case UNDEFINED_TYPE:
                markerOptions.icon(mMarkerGreen).snippet(item.getSnippet());
                break;
        }
    }
}
