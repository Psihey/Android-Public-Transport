package com.provectus.public_transport.model.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public enum ParkingType {

    @SerializedName("OFFICIAL")
    OFFICIAL_TYPE,
    @SerializedName("SEASON")
    SEASON_TYPE,
    @SerializedName("ALLDAY")
    ALLDAY_TYPE,
    @SerializedName("UNDEFINED")
    UNDEFINED_TYPE;

    @TypeConverter
    public static ParkingType fromStringToParkingType(String value) {
        if (Objects.equals(value, "OFFICIAL_TYPE")) {
            return ParkingType.OFFICIAL_TYPE;
        } else if (Objects.equals(value, "SEASON_TYPE")) {
            return ParkingType.SEASON_TYPE;
        } else if (Objects.equals(value, "ALLDAY_TYPE")) {
            return ParkingType.ALLDAY_TYPE;
        } else return ParkingType.UNDEFINED_TYPE;
    }

    @TypeConverter
    public static String parkingTypeToString(ParkingType type) {
        return type == null ? null : type.name();
    }
}
