package com.provectus.public_transport.model.converter;

import android.arch.persistence.room.TypeConverter;


public  class BooleanConverter {
    private BooleanConverter() {
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
