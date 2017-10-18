package com.provectus.public_transport.fragment.routestabfragment;


import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.converter.TransportType;

public interface RoutesTabFragmentPresenter {
    /**
     * A method which binds a view to a presenter.
     *
     * @param routesTabFragment a view whats binds to presenter
     */
    void bindView(RoutesTabFragment routesTabFragment);

    /**
     * A method which unbinds a view to a presenter.
     */
    void unbindView();

    /**
     * A method which sets transport type
     *
     * @param transportType a transport type
     */
    void setTransportType(TransportType transportType);

    /**
     * A method which unregistered Event Bus event
     */
    void unregisteredEventBus();

    void getDataForUpdateRecyclerView(TransportEntity transportEntity);

}