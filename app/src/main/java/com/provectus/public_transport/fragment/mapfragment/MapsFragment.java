package com.provectus.public_transport.fragment.mapfragment;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.provectus.public_transport.model.VehiclesModel;

import java.util.List;

public interface MapsFragment {

    /**
     * A  method which gets route and stops and other parameters for opportunities draw them in map
     *
     * @param routes          a Polyline Options for the draw route on the map
     * @param stopping        a List of MarkerOptions for the draw stops on the map
     * @param transportNumber a number of selected transport
     * @param isChecked       a state select or not selected of the transport
     */
    void drawSelectedPosition(PolylineOptions routes, List<MarkerOptions> stopping, int transportNumber, boolean isChecked);

    /**
     * A method which shows snackbar with some information
     */
    void showErrorSnackbar(int message);

    void drawVehicles(List<VehiclesModel> vehiclesModels);

    void removeVehiclesFromMap();
}
