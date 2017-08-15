package com.provectus.public_transport.service;

import com.provectus.public_transport.model.TransportRoutes;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Psihey on 11.08.2017.
 */

public interface RetrofitQueries {
    // TODO : Change queries when server will be available
    @GET("imcmib/eb92b8df1344e653bb5630b8f3a1c5a7/raw/8351c5cecd2e48ea3481342c2bbb48f10f36473a/gistfile1.txt")
    Observable<List<TransportRoutes>> getAllRoutes();
}
