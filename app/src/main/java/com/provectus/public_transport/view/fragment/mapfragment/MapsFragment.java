package com.provectus.public_transport.view.fragment.mapfragment;

import com.provectus.public_transport.model.TransportRoutes;

import java.util.List;

/**
 * Created by Psihey on 11.08.2017.
 */

public interface MapsFragment {

   void initRecyclerView(List<TransportRoutes> transportRoutes);

   void showDialogError();

}
