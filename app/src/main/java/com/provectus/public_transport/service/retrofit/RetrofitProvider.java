package com.provectus.public_transport.service.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {

    private final static String BASE_URl = "http://46.101.220.157:8080/";
    private static RetrofitQueries sRetrofitQueries;

    private RetrofitProvider() {
    }

    public static RetrofitQueries getRetrofit() {
        if (sRetrofitQueries == null) {
            setUpRetrofit();
        }
        return sRetrofitQueries;
    }

    private static void setUpRetrofit() {
        sRetrofitQueries = new Retrofit.Builder()
                .baseUrl(BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RetrofitQueries.class);
    }

    public static String getBASE_URl() {
        return BASE_URl;
    }
}
