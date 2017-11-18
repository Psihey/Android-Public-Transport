package com.provectus.public_transport.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.model.DirectEntity;
import com.provectus.public_transport.model.IndirectionModel;
import com.provectus.public_transport.model.ParkingEntity;
import com.provectus.public_transport.model.PointEntity;
import com.provectus.public_transport.model.SegmentEntity;
import com.provectus.public_transport.model.StopDetailEntity;
import com.provectus.public_transport.model.StopEntity;
import com.provectus.public_transport.model.StoppingsModel;
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
    private List<TransportEntity> mTransportEntities = new ArrayList<>();
    private List<SegmentEntity> mSegmentEntities = new ArrayList<>();
    private List<PointEntity> mPointEntities = new ArrayList<>();
    private List<StopEntity> mStopEntities = new ArrayList<>();
    private List<DirectEntity> mDirectionEntities = new ArrayList<>();
    private List<TransportEntity> mCurrentFavourites = new ArrayList<>();
    private List<StopDetailEntity> mStopDetailEntities = new ArrayList<>();
    private List<ParkingEntity> mParkingEntities = new ArrayList<>();

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

        Call<List<TransportEntity>> callTransport = RetrofitProvider.getRetrofit().getAllRoutes(date);
        Call<List<StoppingsModel>> callStoppings = RetrofitProvider.getRetrofit().getAllStops();
        Call<List<ParkingEntity>> callParkings = RetrofitProvider.getRetrofit().getAllParkings();

        try {
            Response<List<TransportEntity>> response = callTransport.execute();
            Logger.d(response.code());
            if (response.code() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                Logger.d("There are no updates");
                EventBus.getDefault().post(new BusEvents.DataBaseInitialized());
            } else if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                getAllCurrentFavourite();
                putLastModifiedDateToPreference();
                initTransportEntity(response);
                initStopDetailEntity(callStoppings);
                initParkingEntity(callParkings);
            }
        } catch (IOException e) {
            Logger.d(e.getMessage());
            EventBus.getDefault().post(new BusEvents.ServerNotResponding());
        }

    }

    private void initParkingEntity(Call<List<ParkingEntity>> callParkings) {
        try {
            Response<List<ParkingEntity>> responseParking = callParkings.execute();
            if (responseParking.body() != null || !responseParking.body().isEmpty()) {
                for (ParkingEntity parkingEntity : responseParking.body()) {
                    ParkingEntity currentParkingEntity = new ParkingEntity(parkingEntity.getLatitude(),
                            parkingEntity.getLongitude(),
                            parkingEntity.getPlaces(),
                            parkingEntity.getAddress(),
                            parkingEntity.getType());
                    mParkingEntities.add(currentParkingEntity);
                }
                removeParkingFromDB();
                initParkingToDB();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initStopDetailEntity(Call<List<StoppingsModel>> callStoppings) {
        try {
            Response<List<StoppingsModel>> responseStoppings = callStoppings.execute();
            if (responseStoppings.body() != null || !responseStoppings.body().isEmpty()) {
                for (StoppingsModel currentStop : responseStoppings.body()) {
                    StoppingsModel stoppingsModel = new StoppingsModel(currentStop.getStoppingID());
                    for (StopDetailEntity currentStopDetailEntity : currentStop.getStopDetail()) {
                        StopDetailEntity stopDetailEntity = new StopDetailEntity(stoppingsModel.getStoppingID(),
                                currentStopDetailEntity.getFirstStopping(),
                                currentStopDetailEntity.getLastStopping(),
                                currentStopDetailEntity.getNumber(),
                                currentStopDetailEntity.getTransportType());
                        mStopDetailEntities.add(stopDetailEntity);
                    }

                }
                removeStopDetailsFromDB();
                initStopDetailToDB();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTransportEntity(Response<List<TransportEntity>> responseTransport) {
        if (responseTransport.body() != null || !responseTransport.body().isEmpty()) {
            for (TransportEntity currentRoutes : responseTransport.body()) {
                boolean isAvailable = true;
                boolean isFavourites = false;
                for (TransportEntity transportEntity : mCurrentFavourites) {
                    if (transportEntity.getServerId() == currentRoutes.getServerId()) {
                        isFavourites = true;
                    }
                }
                List<PointEntity> pointsForCurrentRoute = new ArrayList<>();
                TransportEntity currentTransportEntity = new TransportEntity(currentRoutes.getServerId(),
                        currentRoutes.getNumber(),
                        currentRoutes.getType(),
                        currentRoutes.getDistance(),
                        isAvailable,
                        isFavourites,
                        currentRoutes.getCost(),
                        currentRoutes.getFirstStop(),
                        currentRoutes.getLastStop());
                for (DirectEntity directionEntity : currentRoutes.getDirectionEntity()) {
                    DirectEntity currentDirection = new DirectEntity(directionEntity.getLatitude(),
                            directionEntity.getLongitude(),
                            directionEntity.getPosition(),
                            currentTransportEntity.getServerId());
                    mDirectionEntities.add(currentDirection);
                }
                for (IndirectionModel indirectionEntity : currentRoutes.getIndirectionEntity()) {
                    DirectEntity currentIndirection = new DirectEntity(indirectionEntity.getLatitude(),
                            indirectionEntity.getLongitude(),
                            indirectionEntity.getPosition(),
                            currentTransportEntity.getServerId());
                    mDirectionEntities.add(currentIndirection);
                }
                for (SegmentEntity currentSegment : currentRoutes.getSegments()) {
                    SegmentEntity currentSegmentEntity = new SegmentEntity(currentSegment.getServerId(),
                            currentSegment.getDirection(),
                            currentSegment.getPosition(),
                            currentTransportEntity.getServerId());
                    mSegmentEntities.add(currentSegmentEntity);
                    for (PointEntity currentPoint : currentSegment.getPoints()) {
                        pointsForCurrentRoute.add(currentPoint);
                        mPointEntities.add(new PointEntity(currentPoint.getLatitude(),
                                currentPoint.getLongitude(), currentPoint.getPosition(),
                                currentSegmentEntity.getServerId()));
                    }
                    mStopEntities.add(new StopEntity(currentSegment.getStopEntity().getServerId(), currentSegment.getStopEntity().getLatitude(),
                            currentSegment.getStopEntity().getLongitude(),
                            currentSegment.getStopEntity().getTitle(),
                            currentSegmentEntity.getServerId()));
                }
                if (pointsForCurrentRoute.isEmpty()) {
                    isAvailable = false;
                    currentTransportEntity.setIsAvailable(isAvailable);
                }
                mTransportEntities.add(currentTransportEntity);
            }
            removeDataFromDB();
            initDataToDB();
        }
    }

    private void removeParkingFromDB() {
        DatabaseHelper.getPublicTransportDatabase().parkingDao().getAllParkings().subscribe(this::getAllParkingAndRemove);
    }

    private void getAllParkingAndRemove(List<ParkingEntity> parkingEntities) {
        DatabaseHelper.getPublicTransportDatabase().parkingDao().deleteAll(parkingEntities);
    }

    private void initParkingToDB() {
        DatabaseHelper.getPublicTransportDatabase().parkingDao().insertAll(mParkingEntities);
        EventBus.getDefault().post(new BusEvents.DataBaseInitialized());
    }

    private void removeStopDetailsFromDB() {
        DatabaseHelper.getPublicTransportDatabase().stopDetailDao().getAllStopDetail().subscribe(this::getAllStopDetailAndRemove);
    }

    private void getAllStopDetailAndRemove(List<StopDetailEntity> stopDetailEntities) {
        DatabaseHelper.getPublicTransportDatabase().stopDetailDao().deleteAll(stopDetailEntities);
    }

    private void initStopDetailToDB() {
        DatabaseHelper.getPublicTransportDatabase().stopDetailDao().insertAll(mStopDetailEntities);
    }

    private @Nullable String getDateForRequest() {
        SharedPreferences settingCodeResponse = getSharedPreferences(PREFS_NAME, 0);
        String dateLastModified = settingCodeResponse.getString(PREFS_KEY, "");
        if (dateLastModified.isEmpty()) {
            return null;
        } else {
            return dateLastModified;
        }
    }

    private void putLastModifiedDateToPreference() {
        SharedPreferences settingCodeResponse = getSharedPreferences(PREFS_NAME, 0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RFC_1123_DATE_TIME, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_GMT));
        SharedPreferences.Editor editor = settingCodeResponse.edit();
        editor.putString(PREFS_KEY, simpleDateFormat.format(new Date()));
        editor.apply();
    }

    private void removeDataFromDB() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().getFavouritesRouteBeforeDeleteDB();
        DatabaseHelper.getPublicTransportDatabase().transportDao().deleteAll(mTransportEntities);
        DatabaseHelper.getPublicTransportDatabase().segmentDao().deleteAll(mSegmentEntities);
        DatabaseHelper.getPublicTransportDatabase().pointDao().deleteAll(mPointEntities);
        DatabaseHelper.getPublicTransportDatabase().stopDao().deleteAll(mStopEntities);
        DatabaseHelper.getPublicTransportDatabase().directionDao().deleteAll(mDirectionEntities);
    }

    private void initDataToDB() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().insertAll(mTransportEntities);
        DatabaseHelper.getPublicTransportDatabase().segmentDao().insertAll(mSegmentEntities);
        DatabaseHelper.getPublicTransportDatabase().pointDao().insertAll(mPointEntities);
        DatabaseHelper.getPublicTransportDatabase().stopDao().insertAll(mStopEntities);
        DatabaseHelper.getPublicTransportDatabase().directionDao().insertAll(mDirectionEntities);
    }

    private void getAllCurrentFavourite() {
        DatabaseHelper.getPublicTransportDatabase().transportDao().getFavouritesRouteBeforeDeleteDB()
                .doOnError(throwable -> Logger.d(throwable.getMessage()))
                .subscribe(this::getAllFavouritesFromDB);
    }

    private void getAllFavouritesFromDB(List<TransportEntity> transportEntities) {
        mCurrentFavourites = transportEntities;
    }

}
