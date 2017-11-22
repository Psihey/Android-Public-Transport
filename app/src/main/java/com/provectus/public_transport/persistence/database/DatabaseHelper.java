package com.provectus.public_transport.persistence.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseHelper {

    private DatabaseHelper() {
    }

    private static PublicTransportDatabase sPublicTransportDatabase;

    public static PublicTransportDatabase createPublicTransportDatabase(Context context) {
        if (sPublicTransportDatabase == null) {
            sPublicTransportDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    PublicTransportDatabase.class, "public_transport").build();
        }
        return sPublicTransportDatabase;
    }

    public static PublicTransportDatabase getPublicTransportDatabase() {
        return sPublicTransportDatabase;
    }
}
