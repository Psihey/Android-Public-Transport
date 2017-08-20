package com.provectus.public_transport;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.crashlytics.android.Crashlytics;
import com.provectus.public_transport.persistent.database.DatabaseHelper;
import com.provectus.public_transport.persistent.database.PublicTransportDatabase;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Psihey on 15.08.2017.
 */

public class PublicTransportApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        DatabaseHelper.getDatabase(getApplicationContext());
    }

}
