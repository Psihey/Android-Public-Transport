package com.provectus.public_transport.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.model.TransportRoutes;
import com.provectus.public_transport.service.retrofit.RetrofitProvider;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Psihey on 20.08.2017.
 */

public class TransportRoutesService extends Service {

    private CompositeDisposable mCompositeDisposable;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mCompositeDisposable = new CompositeDisposable();
        getRoutesFromServer();
        Logger.d("Service is started");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        Logger.d("Service is Destroyed");
    }

    private void getRoutesFromServer() {
        mCompositeDisposable.add(RetrofitProvider
                .getRetrofit().getAllRoutes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(List<TransportRoutes> transportRoutes) {
        Logger.d("All Ok, we got responce");
        EventBus.getDefault().post(new BusEvents.SendRoutesEvent(transportRoutes));
//        DatabaseHelper.getPublicTransportDatabase().transportDao().getAllTransport().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::getFromDb;
//          Completable.fromAction(this:: putToDB);
    }

    private void handleError(Throwable throwable) {
        Logger.d("Handle Error from when fetching data" + throwable.getMessage());
    }

//    private void putToDB() {
//        List<TransportEntity> transportEntities = new ArrayList<>();
//        transportEntities.add(new TransportEntity(2,"tram","ss"));
//        transportEntities.add(new TransportEntity(3,"tram2","s2s"));
//        DatabaseHelper.getPublicTransportDatabase().transportDao().insertAll(transportEntities);
//        DatabaseHelper.getPublicTransportDatabase().transportDao().delete(new TransportEntity(2, "tram", "ss"));
//    }

//    private void getFromDb(List<TransportEntity> transportEntities) {
//        System.out.println(transportEntities.toString());
//    }
}
