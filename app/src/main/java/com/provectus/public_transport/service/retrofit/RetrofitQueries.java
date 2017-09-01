package com.provectus.public_transport.service.retrofit;

import com.google.common.net.HttpHeaders;
import com.provectus.public_transport.model.TransportEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by Psihey on 11.08.2017.
 */

public interface RetrofitQueries {

    @GET("routes")
    Call<List<TransportEntity>> getAllRoutes(@Header(HttpHeaders.IF_MODIFIED_SINCE) String date);
}
