package com.provectus.public_transport.view.fragment.mapfragment;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Psihey on 11.08.2017.
 */

public interface MapsFragment {

    void setIconInTabLayout();

    void changeIconInTabLayout(int position);

    void drawRotes(List<LatLng> routes);

}
