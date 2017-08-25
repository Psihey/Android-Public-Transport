package com.provectus.public_transport.fragment.routestabfragment;

import com.provectus.public_transport.model.TransportType;

/**
 * Created by Evgeniy on 8/23/2017.
 */

public interface RoutesTabFragmentPresenter {

    void bindView(RoutesTabFragment routesTabFragment);

    void unbindView();

    void setTransportType(TransportType transportType);

}
