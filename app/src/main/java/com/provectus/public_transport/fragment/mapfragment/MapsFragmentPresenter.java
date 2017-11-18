package com.provectus.public_transport.fragment.mapfragment;


import com.provectus.public_transport.model.TransportEntity;

public interface MapsFragmentPresenter {

    /**
     * A method which binds a view to a presenter.
     *
     * @param mapsFragment a view whats binds to presenter
     */
    void bindView(MapsFragment mapsFragment);

    /**
     * A method which unbinds a view to a presenter.
     */
    void unbindView();

    void onSelectCurrentRoute(TransportEntity route);

    void getRouteInformation(TransportEntity transportEntity);

    void stopGetVehicles();

    void getAllParking();
}