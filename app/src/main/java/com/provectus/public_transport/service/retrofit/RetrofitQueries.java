package com.provectus.public_transport.service.retrofit;

import com.google.common.net.HttpHeaders;
import com.provectus.public_transport.model.TransportEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface RetrofitQueries {

    @GET("routes")
    Call<List<TransportEntity>> getAllRoutes(@Header(HttpHeaders.IF_MODIFIED_SINCE) String date);
}
