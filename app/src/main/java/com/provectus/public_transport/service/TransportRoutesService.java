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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

public class TransportRoutesService extends IntentService {

    private static final String RFC_1123_DATE_TIME = "EEE, dd MMM 2016 HH:mm:ss z";
    private List<TransportEntity> mTransportEntity = new ArrayList<>();
    private List<SegmentEntity> mSegmentEntity = new ArrayList<>();
    private List<PointEntity> mPointEntity = new ArrayList<>();
    private List<StopEntity> mStopEntity = new ArrayList<>();

    public TransportRoutesService() {
        super(TransportRoutesService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("Service is Created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("Service is Destroyed");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        getRoutesFromServer();
    }

    private void getRoutesFromServer() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RFC_1123_DATE_TIME, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Call<List<TransportEntity>> call = RetrofitProvider.getRetrofit().getAllRoutes(simpleDateFormat.format(new Date()));
        Logger.d(simpleDateFormat.format(new Date()));
        try {
            Response<List<TransportEntity>> response = call.execute();
            Logger.d(response.code());
            if (response.isSuccessful() && response.code() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                Logger.d("There are no updates");
            } else if (response.isSuccessful() && response.body() != null) {
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
            } else {
                Logger.d("Response is failed");
            }

        } catch (IOException e) {
            Logger.d(e.getMessage());
        }

    }

    private void removeAllFromTables() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().deleteAll(mTransportEntity);
        DatabaseHelper.getPublicTransportDatabase().segmentDao().deleteAll(mSegmentEntity);
        DatabaseHelper.getPublicTransportDatabase().pointDao().deleteAll(mPointEntity);
        DatabaseHelper.getPublicTransportDatabase().stopDao().deleteAll(mStopEntity);
    }

    private void initDataToDataBase() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().insertAll(mTransportEntity);
        DatabaseHelper.getPublicTransportDatabase().segmentDao().insertAll(mSegmentEntity);
        DatabaseHelper.getPublicTransportDatabase().pointDao().insertAll(mPointEntity);
        DatabaseHelper.getPublicTransportDatabase().stopDao().insertAll(mStopEntity);
        EventBus.getDefault().post(new BusEvents.DataBaseInitialized());
        Logger.d("Database is initialized");
    }

}
