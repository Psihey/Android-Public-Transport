package com.provectus.public_transport.fragment.favouritesfragment;

import com.provectus.public_transport.model.TransportEntity;

import java.util.List;

public interface FavouritesFragment {

    void initRecyclerView(List<TransportEntity> transportEntity);

    void updateData(List<TransportEntity> transportList);
}