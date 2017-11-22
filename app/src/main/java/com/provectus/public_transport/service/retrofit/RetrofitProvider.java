package com.provectus.public_transport.service.retrofit;

import com.provectus.public_transport.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {

    private static final String BASE_URL = BuildConfig.SERVER_URL;
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
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RetrofitQueries.class);
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }
}
