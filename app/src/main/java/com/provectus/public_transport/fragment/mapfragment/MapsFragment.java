package com.provectus.public_transport.fragment.mapfragment;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Psihey on 11.08.2017.
 */

public interface MapsFragment {

    void drawRotes(List<LatLng> routes);


}
