package com.provectus.public_transport.fragment.favouritesfragment;

import com.provectus.public_transport.model.TransportEntity;

import java.util.List;

/**
 * Created by Psihey on 13.10.2017.
 */

public interface FavouritesFragment {

    void initRecyclerView(List<TransportEntity> transportEntity);
}
