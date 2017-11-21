package com.provectus.public_transport;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.provectus.public_transport.persistence.database.DatabaseHelper;

import io.fabric.sdk.android.Fabric;

public class PublicTransportApplication extends Application {

    static private Context sContext;
    
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        DatabaseHelper.createPublicTransportDatabase(getApplicationContext());
        sContext = getApplicationContext();
        getApplicationContext();
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static Context getContext(){
        return sContext;
    }

}
