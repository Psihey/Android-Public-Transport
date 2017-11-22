package com.provectus.public_transport.fragment.mapfragment;

import com.google.android.gms.maps.model.PolylineOptions;
import com.provectus.public_transport.model.ParkingEntity;
import com.provectus.public_transport.model.StopEntity;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.VehicleMarkerInfoModel;
import com.provectus.public_transport.model.VehiclesModel;

import java.util.List;

public interface MapsFragment {

    /**
     * A method which shows snackbar with some information
     */
    void showErrorSnackbar(int message);

    void drawVehicles(List<VehiclesModel> vehiclesModels);

    void removeVehiclesFromMap();

    void drawStops(List<StopEntity> stopping);

    void getInfoTransport(int transportNumber, long transportId);

    boolean checkOnReadyMap();

    void drawRoutesWithDirection(PolylineOptions routes);

    void getColorForRoute();

    void removeStopsFromMap();

    void removeRoutesWithDirectionFromMap();

    void sendVehicleInfo(VehicleMarkerInfoModel vehicleMarkerInfoModel);

    void routeNotSelected();

    void openRouteInfo(TransportEntity transportEntity);

    void addAllParkingToCluster(List<ParkingEntity> parkingEntities);

}