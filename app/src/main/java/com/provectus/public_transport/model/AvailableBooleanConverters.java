package com.provectus.public_transport.model;

import android.arch.persistence.room.TypeConverter;

/**
 * Created by Psihey on 07.09.2017.
 */

public class AvailableBooleanConverters {
    private AvailableBooleanConverters() {

    }

    @TypeConverter
    public static boolean fromIntToBoolean(int value) {
        return value != 0;
    }

    @TypeConverter
    public static int booleanToInt(boolean value) {
        return value ? 1 : 0;
    }
}
