package com.provectus.public_transport.fragment.mapfragment;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.provectus.public_transport.model.VehiclesModel;

import java.util.List;

public interface MapsFragment {

    /**
     * A method which shows snackbar with some information
     */
    void showErrorSnackbar(int message);

    void drawVehicles(List<VehiclesModel> vehiclesModels);

    void removeVehiclesFromMap();

    void drawStops(List<MarkerOptions> stopping);

    void getInfoTransport(int transportNumber, boolean isChecked);

    boolean checkOnReadyMap();

    void drawRoutesWithDirection(PolylineOptions routes);
}
