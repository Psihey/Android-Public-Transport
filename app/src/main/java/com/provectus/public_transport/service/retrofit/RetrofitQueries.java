package com.provectus.public_transport.service.retrofit;

import android.support.annotation.Nullable;

import com.google.common.net.HttpHeaders;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.VehiclesModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RetrofitQueries {

    @GET("routes")
    Call<List<TransportEntity>> getAllRoutes(@Nullable @Header(HttpHeaders.IF_MODIFIED_SINCE) String date);

    @GET("location")
    Observable<List<VehiclesModel>> getAllVehiclesForRoute(@Query("route[]") ArrayList<Long> routesId);
}
