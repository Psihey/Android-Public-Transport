package com.provectus.public_transport.service.retrofit;

import com.provectus.public_transport.model.TransportRoutes;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Psihey on 11.08.2017.
 */

public interface RetrofitQueries {

    @GET("routes")
    Observable<List<TransportRoutes>> getAllRoutes();
}
