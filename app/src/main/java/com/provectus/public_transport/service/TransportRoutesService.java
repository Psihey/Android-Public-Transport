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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

/**
 * Created by Psihey on 20.08.2017.
 */
public class TransportRoutesService extends IntentService {

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        System.out.println(dateFormat.format(new Date()));
        Call<List<TransportEntity>> call = RetrofitProvider.getRetrofit().getAllRoutes(dateFormat.format(new Date()));
        try {
            for (TransportEntity currentRoutes : call.execute().body()) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        removeAllFromTables();
        initDataToDataBase();
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
    }

}
