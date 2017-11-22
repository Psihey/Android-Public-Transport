package com.provectus.public_transport.fragment.favouritesfragment;

import com.provectus.public_transport.model.TransportEntity;

import java.util.List;


public interface FavouritesFragmentPresenter {

    void bindView(FavouritesFragment favouritesFragment);

    void unbindView();

    void deleteFavourites(TransportEntity transportEntity);

    void updateRecyclerView(List<TransportEntity> transportEntities);

    void unregisteredEventBus();
}