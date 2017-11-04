package com.provectus.public_transport.utils;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.provectus.public_transport.model.ParkingEntity;

public class CustomClusterRenderer extends DefaultClusterRenderer<ParkingEntity> {
    private final Context mContext;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<ParkingEntity> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }


    @Override
    protected void onClusterItemRendered(ParkingEntity clusterItem, Marker marker) {
        marker.setTag(clusterItem);
    }

    @Override
    protected void onBeforeClusterItemRendered(ParkingEntity item, MarkerOptions markerOptions) {
        final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        final BitmapDescriptor markerDescriptor1 = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        final BitmapDescriptor markerDescriptor2 = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
        final BitmapDescriptor markerDescriptor3 = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);

        switch (item.getType()) {
            case OFFICIAL_TYPE:
                markerOptions.icon(markerDescriptor).snippet(item.getSnippet());
                break;
            case ALLDAY_TYPE:
                markerOptions.icon(markerDescriptor1).snippet(item.getSnippet());
                break;
            case SEASON_TYPE:
                markerOptions.icon(markerDescriptor2).snippet(item.getSnippet());
                break;
            case UNDEFINED_TYPE:
                markerOptions.icon(markerDescriptor3).snippet(item.getSnippet());
                break;
        }
    }
}
