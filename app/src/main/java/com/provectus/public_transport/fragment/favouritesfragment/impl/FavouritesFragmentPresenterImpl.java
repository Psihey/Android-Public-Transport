package com.provectus.public_transport.fragment.favouritesfragment.impl;

import com.orhanobut.logger.Logger;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.fragment.favouritesfragment.FavouritesFragment;
import com.provectus.public_transport.fragment.favouritesfragment.FavouritesFragmentPresenter;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.persistence.database.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Psihey on 13.10.2017.
 */

public class FavouritesFragmentPresenterImpl implements FavouritesFragmentPresenter {

    private FavouritesFragment mFavouritesFragment;
    private List<TransportEntity> mListForUpdate;
    private List<TransportEntity> mAllSelectedRoutes = new ArrayList<>();

    @Override
    public void bindView(FavouritesFragment favouritesFragment) {
        this.mFavouritesFragment = favouritesFragment;
        EventBus.getDefault().register(this);
        getAllFavouritesFromDB();
    }

    @Override
    public void unbindView() {
        mFavouritesFragment = null;
        EventBus.getDefault().unregister(this);
    }

    private void getAllFavouritesFromDB() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().getFavouritesRoute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Logger.d(throwable.getMessage()))
                .subscribe(this::getFavouritesFromDB);
    }

    private void getFavouritesFromDB(List<TransportEntity> transportEntities) {
        if (transportEntities == null) {
            return;
        }
        if (mListForUpdate != null) {
            for (TransportEntity newEntity : transportEntities) {
                for (TransportEntity recentEntity : mListForUpdate) {
                    if (newEntity.getServerId() == recentEntity.getServerId()) {
                        newEntity.setIsSelected(recentEntity.isSelected());
                    }
                }
                if (mAllSelectedRoutes != null){
                    for (TransportEntity newClickEntity : mAllSelectedRoutes){
                        if (newEntity.getServerId() == newClickEntity.getServerId()){
                            newEntity.setIsSelected(newClickEntity.isSelected());
                        }
                    }
                }
            }
            mListForUpdate.clear();
            mListForUpdate.addAll(transportEntities);
        } else {
                mListForUpdate = transportEntities;
        }
        if (mFavouritesFragment != null) {
            mFavouritesFragment.initRecyclerView(transportEntities);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteFavourites(BusEvents.DeleteFavourites event) {
        TransportEntity transportEntity = event.getSelectRout();
        transportEntity.setIsFavourites(false);
        new Thread(() -> DatabaseHelper.getPublicTransportDatabase().transportDao().updateFavourites(transportEntity)).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateRecyclerView(BusEvents.UpdateDataFavouritesRecyclerView event) {
        mFavouritesFragment.updateData(event.getTransportData());
        for (TransportEntity transportEntity : event.getTransportData()){
            if (transportEntity.isSelected()){
                mAllSelectedRoutes.add(transportEntity);
            }
        }
    }

}