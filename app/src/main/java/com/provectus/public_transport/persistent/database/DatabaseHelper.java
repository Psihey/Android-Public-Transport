package com.provectus.public_transport.persistent.database;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by Psihey on 20.08.2017.
 */

public class DatabaseHelper {

    private static PublicTransportDatabase sPublicTransportDatabase;

    public static PublicTransportDatabase getDatabase(Context context){
        if (sPublicTransportDatabase == null){
            sPublicTransportDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    PublicTransportDatabase.class,"public_transport").build();
        }
        return sPublicTransportDatabase;
    }

    public static PublicTransportDatabase getPublicTransportDatabase() {
        return sPublicTransportDatabase;
    }
}
