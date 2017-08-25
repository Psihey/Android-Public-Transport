package com.provectus.public_transport.model;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Created by Psihey on 15.08.2017.
 */

public enum TransportType {

    @SerializedName("tram")
    TRAM_TYPE,
    @SerializedName("trolleybuses")
    TROLLEYBUSES_TYPE,
    @SerializedName("taxi")
    PARKING_TYPE;

    @TypeConverter
    public static TransportType fromTimestamp(String value) {
        if (Objects.equals(value, "TRAM_TYPE")) {
            return TransportType.TRAM_TYPE;
        } else if (Objects.equals(value, "TROLLEYBUSES_TYPE")) {
            return TransportType.TROLLEYBUSES_TYPE;
        } else
            return TransportType.PARKING_TYPE;
    }

    @TypeConverter
    public static String dateToTimestamp(TransportType date) {
        return date == null ? null : date.name();
    }
}
