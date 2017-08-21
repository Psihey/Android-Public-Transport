package com.provectus.public_transport.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.provectus.public_transport.model.Point;
import com.provectus.public_transport.model.Segment;
import com.provectus.public_transport.model.TransportRoutes;
import com.provectus.public_transport.persistence.database.DatabaseHelper;
import com.provectus.public_transport.persistence.entity.PointEntity;
import com.provectus.public_transport.persistence.entity.SegmentEntity;
import com.provectus.public_transport.persistence.entity.TransportEntity;
import com.provectus.public_transport.service.retrofit.RetrofitProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Psihey on 20.08.2017.
 */

public class TransportRoutesService extends Service {

    private CompositeDisposable mCompositeDisposable;
    private List<TransportEntity> transportEntities = new ArrayList<>();
    private List<SegmentEntity> segmentEntity = new ArrayList<>();
    private List<PointEntity> pointEntity = new ArrayList<>();

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
                .repeatWhen(objectObservable -> objectObservable.delay(60, TimeUnit.MINUTES))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(List<TransportRoutes> transportRoutes) {
        Logger.d("All Ok, we got responce");
        int transportId = 1;
        int segmentId = 1;
        for (TransportRoutes currentRoutes : transportRoutes) {
            transportEntities.add(new TransportEntity(currentRoutes.getNumber(), currentRoutes.getType().name(), currentRoutes.getDistance()));
            for (Segment currentSegment : currentRoutes.getSegment()) {
                segmentEntity.add(new SegmentEntity(currentSegment.getPosition(), currentSegment.getDirection(), transportId));
                for (Point currentPoint : currentSegment.getPoints()) {
                    pointEntity.add(new PointEntity(currentPoint.getPosition(), currentPoint.getLatitude(), currentPoint.getLongitude(), segmentId));
                }
                segmentId++;
            }
            transportId++;
        }

        DatabaseHelper.getPublicTransportDatabase().transportDao().getAllTransport()
                .subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getFromDb);
    }

    private void getFromDb(List<TransportEntity> transportEntities) {
        if (transportEntities.isEmpty()) {
            Completable.defer(() -> Completable.fromCallable(this::putToDB))
                    .subscribeOn(Schedulers.computation())
                    .subscribe(
                            () -> {
                            },
                            throwable -> Logger.d(throwable.getMessage())
                    );
            Logger.d("Database is initialized");
        }else {
            Completable.defer(() -> Completable.fromCallable(this::updateDB))
                    .subscribeOn(Schedulers.computation())
                    .subscribe(
                            () -> {
                            },
                            throwable -> Logger.d(throwable.getMessage())
                    );
        }
    }

    private Object updateDB() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().updateTransport(transportEntities);
        DatabaseHelper.getPublicTransportDatabase().segmentDao().updateSegment(segmentEntity);
        DatabaseHelper.getPublicTransportDatabase().pointDao().updatePoint(pointEntity);
        Logger.d("Databse is Updated");
        return true;
    }

    private void handleError(Throwable throwable) {
        Logger.d("Handle Error from when fetching data" + throwable.getMessage());
    }

    private boolean putToDB() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().insertAll(transportEntities);
        DatabaseHelper.getPublicTransportDatabase().segmentDao().insertAll(segmentEntity);
        DatabaseHelper.getPublicTransportDatabase().pointDao().insertAll(pointEntity);
        return true;
    }

}
