package com.provectus.public_transport.fragment.mapfragment;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by Psihey on 11.08.2017.
 */

public interface MapsFragment {

    void drawSelectedPosition(PolylineOptions routes, List<MarkerOptions> stopping, int transportNumber, boolean isChecked);

    void showErrorSnackbar();

}
