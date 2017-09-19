package com.provectus.public_transport.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Psihey on 19.09.2017.
 */

public class IndirectionModel {

    @SerializedName("lat")
    private double mLatitude;

    @SerializedName("lng")
    private double mLongitude;

    @SerializedName("position")
    private int mPosition;

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public int getPosition() {
        return mPosition;
    }

    @Override
    public String toString() {
        return "IndirectionModel{" +
                "mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mPosition=" + mPosition +
                '}';
    }
}
