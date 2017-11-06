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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FavouritesFragmentPresenterImpl implements FavouritesFragmentPresenter {

    private FavouritesFragment mFavouritesFragment;
    private List<TransportEntity> mListForUpdate;
    private List<TransportEntity> mAllSelectedRoutes = new ArrayList<>();
    private TransportEntity mTransportRoute;

    @Override
    public void bindView(FavouritesFragment favouritesFragment) {
        this.mFavouritesFragment = favouritesFragment;
        EventBus.getDefault().register(this);
        EventBus.getDefault().postSticky(new BusEvents.SendFavouriteFragmentPresenter(this));
        getAllFavouritesFromDB();
    }

    @Override
    public void unbindView() {
        mFavouritesFragment = null;
    }

    @Override
    public void deleteFavourites(TransportEntity transportRoute) {
        mTransportRoute = transportRoute;
        mTransportRoute.setIsFavourites(false);

        Completable.defer(() -> Completable.fromCallable(this::updateFavouritesDB))
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        () -> {
                        },
                        throwable -> Logger.d(throwable.getMessage())
                );
    }

    private boolean updateFavouritesDB() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().updateFavourites(mTransportRoute);
        return true;
    }

    @Override
    public void updateRecyclerView(List<TransportEntity> transportEntities) {
        mFavouritesFragment.updateData(transportEntities);
        for (TransportEntity transportEntity : transportEntities) {
            if (transportEntity.isSelected()) {
                mAllSelectedRoutes.add(transportEntity);
            }
        }
    }

    @Override
    public void unregisteredEventBus() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateRecyclerView(BusEvents.UnselectedAllItems updateRecyclerView) {
        mListForUpdate = null;
        getAllFavouritesFromDB();
    }

    private void getAllFavouritesFromDB() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().getFavouritesRoute()
                .map(list -> {
                    Collections.sort(list, sortByNumber);
                    return list;
                })
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
                if (mAllSelectedRoutes != null) {
                    for (TransportEntity newClickEntity : mAllSelectedRoutes) {
                        if (newEntity.getServerId() == newClickEntity.getServerId()) {
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

    private Comparator<TransportEntity> sortByNumber = (t1, t2) -> t1.getNumber() > t2.getNumber() ? 1 : -1;
}