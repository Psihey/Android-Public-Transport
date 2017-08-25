package com.provectus.public_transport.fragment.routestabfragment;

import com.provectus.public_transport.model.TransportEntity;

import java.util.List;

/**
 * Created by Evgeniy on 8/23/2017.
 */

public interface RoutesTabFragment {

    void initRecyclerView(List<TransportEntity> transportEntity);

}
