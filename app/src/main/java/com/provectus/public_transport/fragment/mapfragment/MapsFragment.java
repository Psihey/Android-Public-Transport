package com.provectus.public_transport.fragment.mapfragment;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.Map;

/**
 * Created by Psihey on 11.08.2017.
 */

public interface MapsFragment {

    void drawSelectedPosition(Map<Integer, PolylineOptions> routes, Map<Integer, List<MarkerOptions>> stopping);

    void showErrorSnackbar();

}
