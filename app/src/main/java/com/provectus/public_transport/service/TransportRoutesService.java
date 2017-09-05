package com.provectus.public_transport.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private static final String RFC_1123_DATE_TIME = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final String TIME_ZONE_GMT = "GMT";
    private static final String PREFS_NAME = "HttpCodePrefs";
    private static final String PREFS_KEY = "code";
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
        EventBus.getDefault().post(new BusEvents.ServiceEndWorked());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        getRoutesFromServer();
    }

    private void getRoutesFromServer() {


        String date = getDateForRequest();

        Call<List<TransportEntity>> call = RetrofitProvider.getRetrofit().getAllRoutes(date);

        try {
            Response<List<TransportEntity>> response = call.execute();
            Logger.d(response.code());
            if (response.isSuccessful() && response.code() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                Logger.d("There are no updates");
                EventBus.getDefault().post(new BusEvents.DataBaseInitialized());
            } else if (response.isSuccessful() && response.body() != null) {
                putLastModifiedDateToPreference();
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
        } catch (IOException e) {
            Logger.d(e.getMessage());
        }

    }

    private String getDateForRequest() {
        SharedPreferences PREF_DATA_LAST_MODIFIED = getSharedPreferences(PREFS_NAME, 0);
        if (PREF_DATA_LAST_MODIFIED.getString(PREFS_KEY, "").isEmpty()) {
            return null;
        } else {
            return PREF_DATA_LAST_MODIFIED.getString(PREFS_KEY, "");
        }
    }

    private void putLastModifiedDateToPreference() {
        SharedPreferences PREF_DATA_LAST_MODIFIED = getSharedPreferences(PREFS_NAME, 0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RFC_1123_DATE_TIME, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_GMT));
        SharedPreferences.Editor editor = PREF_DATA_LAST_MODIFIED.edit();
        editor.putString(PREFS_KEY, simpleDateFormat.format(new Date()));
        editor.apply();
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
