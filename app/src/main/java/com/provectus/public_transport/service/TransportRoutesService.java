package com.provectus.public_transport.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.model.PointEntity;
import com.provectus.public_transport.model.SegmentEntity;
import com.provectus.public_transport.model.StopEntity;
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
    private static final int CODE_CHECK_FOR_UPDATE = 304;
    private CompositeDisposable mCompositeDisposable;
    private List<TransportEntity> mTransportEntity = new ArrayList<>();
    private List<SegmentEntity> mSegmentEntity = new ArrayList<>();
    private List<PointEntity> mPointEntity = new ArrayList<>();
    private List<StopEntity> mStopEntity = new ArrayList<>();

    public TransportRoutesService() {
        super(TransportRoutesService.class.getName());
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
        if (response.code() == CODE_CHECK_FOR_UPDATE) {
            Logger.d("We got 304 Code Data not modified");
        } else {
            for (TransportEntity currentRoutes : response.body()) {
                TransportEntity currentTransportEntity = new TransportEntity(currentRoutes.getServerId(),
                        currentRoutes.getNumber(),
                        currentRoutes.getType(),
                        currentRoutes.getDistance());
                mTransportEntity.add(currentTransportEntity);
                for (SegmentEntity currentSegment : currentRoutes.getSegments()) {
                    SegmentEntity currentSegmentEntity = new SegmentEntity(currentSegment.getServerId(),
                            currentSegment.getDirection(),
                            currentSegment.getPosition(),
                            currentTransportEntity.getServerId());
                    mSegmentEntity.add(currentSegmentEntity);
                    for (PointEntity currentPoint : currentSegment.getPoints()) {
                        mPointEntity.add(new PointEntity(currentPoint.getLatitude(),
                                currentPoint.getLongitude(), currentPoint.getPosition(),
                                currentSegmentEntity.getServerId()));
                    }
                    mStopEntity.add(new StopEntity(currentSegment.getStopEntity().getLatitude(),
                            currentSegment.getStopEntity().getLongitude(),
                            currentSegment.getStopEntity().getTitle(),
                            currentSegmentEntity.getServerId()));
                }
            }
            removeAllFromTables();
            initDataToDataBase();
        }

        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    private void handleError(Throwable throwable) {
        Logger.d("Handle Error from when fetching data" + throwable.getMessage());
        EventBus.getDefault().post(new BusEvents.SendRoutesErrorEvent());
    }

    private void removeAllFromTables() {
        Completable.defer(() -> Completable.fromCallable(this::deleteFromDataBase))
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        () -> {
                        },
                        throwable -> Logger.d(throwable.getMessage())
                );
        Logger.d("Database is removed");
    }

    private boolean deleteFromDataBase() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().deleteAll();
        DatabaseHelper.getPublicTransportDatabase().segmentDao().deleteAll();
        DatabaseHelper.getPublicTransportDatabase().pointDao().deleteAll();
        DatabaseHelper.getPublicTransportDatabase().stopDao().deleteAll();
        return true;
    }

    private void initDataToDataBase() {
        Completable.defer(() -> Completable.fromCallable(this::putToDB))
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        () -> {
                        },
                        throwable -> Logger.d(throwable.getMessage())
                );
        Logger.d("Database is initialized");
    }

    private boolean putToDB() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().insertAll(mTransportEntity);
        DatabaseHelper.getPublicTransportDatabase().segmentDao().insertAll(mSegmentEntity);
        DatabaseHelper.getPublicTransportDatabase().pointDao().insertAll(mPointEntity);
        DatabaseHelper.getPublicTransportDatabase().stopDao().insertAll(mStopEntity);
        return true;
    }

}
