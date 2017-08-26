package com.provectus.public_transport.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.model.PointEntity;
import com.provectus.public_transport.model.SegmentEntity;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.persistence.database.DatabaseHelper;
import com.provectus.public_transport.service.retrofit.RetrofitProvider;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by Psihey on 20.08.2017.
 */
public class TransportRoutesService extends IntentService {

    private CompositeDisposable mCompositeDisposable;
    private List<TransportEntity> mTransportEntity = new ArrayList<>();
    private List<SegmentEntity> mSegmentEntity = new ArrayList<>();
    private List<PointEntity> mPointEntity = new ArrayList<>();

    public TransportRoutesService() {
        super("TransportRoutesService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCompositeDisposable = new CompositeDisposable();
        Logger.d("Service is Created");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Logger.d("Service is HandleIntent");
        getRoutesFromServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("Service is Destroyed");
    }

    private void getRoutesFromServer() {
        mCompositeDisposable.add(RetrofitProvider
                .getRetrofit().getAllRoutes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response<List<TransportEntity>> response) {
        Logger.d("All Ok, we got responce");
        if (response.body() != null) {
            for (TransportEntity currentRoutes : response.body()) {
                TransportEntity currentTransportEntity = new TransportEntity(currentRoutes.getmServerId(), currentRoutes.getNumber(), currentRoutes.getType(), currentRoutes.getDistance());
                mTransportEntity.add(currentTransportEntity);
                for (SegmentEntity currentSegment : currentRoutes.getSegments()) {
                    SegmentEntity currentSegmentEntity = new SegmentEntity(currentSegment.getServerId(), currentSegment.getDirection(), currentSegment.getPosition(), currentTransportEntity.getmServerId());
                    mSegmentEntity.add(currentSegmentEntity);
                    for (PointEntity currentPoint : currentSegment.getPoints()) {
                        mPointEntity.add(new PointEntity(currentPoint.getLatitude(), currentPoint.getLongitude(), currentPoint.getPosition(), currentSegmentEntity.getServerId()));
                    }
                }
            }
        }
        puttDataToDataBase();
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    private void handleError(Throwable throwable) {
        Logger.d("Handle Error from when fetching data" + throwable.getMessage());
        EventBus.getDefault().post(new BusEvents.SendRoutesErrorEvent());
    }

    private void puttDataToDataBase() {
        Completable.defer(() -> Completable.fromCallable(this::putToDB))
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        () -> {
                        },
                        throwable -> Logger.d(throwable.getMessage())
                );
        Logger.d("Database is initialized");
        EventBus.getDefault().post(new BusEvents.SendRoutesEvent());
    }

    private boolean putToDB() {
        for (TransportEntity current:
             mTransportEntity) {
            System.out.println(current);
        }
        DatabaseHelper.getPublicTransportDatabase().transportDao().insertAll(mTransportEntity);
        DatabaseHelper.getPublicTransportDatabase().segmentDao().insertAll(mSegmentEntity);
        DatabaseHelper.getPublicTransportDatabase().pointDao().insertAll(mPointEntity);
        return true;
    }

}
