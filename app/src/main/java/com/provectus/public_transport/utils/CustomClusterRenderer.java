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
    final BitmapDescriptor markerBlue = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pin_blue);
    final BitmapDescriptor markerRed = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pin_red);
    final BitmapDescriptor markerYellow = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pin_orange);
    final BitmapDescriptor markerGreen = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pin_green);

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
                markerOptions.icon(markerYellow).snippet(item.getSnippet());
                break;
            case ALLDAY_TYPE:
                markerOptions.icon(markerBlue).snippet(item.getSnippet());
                break;
            case SEASON_TYPE:
                markerOptions.icon(markerRed).snippet(item.getSnippet());
                break;
            case UNDEFINED_TYPE:
                markerOptions.icon(markerGreen).snippet(item.getSnippet());
                break;
        }
    }
}
