package com.provectus.public_transport.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

@Entity(tableName = "parking")
public class ParkingEntity implements ClusterItem {

    @PrimaryKey(autoGenerate = true)
    private long mId;

    @SerializedName("lat")
    @ColumnInfo(name = "latitude")
    private double mLatitude;

    @SerializedName("lng")
    @ColumnInfo(name = "longitude")
    private double mLongitude;

    @SerializedName("places")
    @ColumnInfo(name = "places")
    private int mPlaces;

    @SerializedName("adress")
    @ColumnInfo(name = "adress")
    private String mAddress;

    @SerializedName("type")
    @ColumnInfo(name = "type")
    private String mType;

    @Ignore
    private final LatLng mPosition;

    public ParkingEntity(double latitude, double longitude, int places, String address, String type) {
        mPosition = new LatLng(latitude, longitude);
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mPlaces = places;
        this.mAddress = address;
        this.mType = type;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getId() {
        return mId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public int getPlaces() {
        return mPlaces;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getType() {
        return mType;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    @Override
    public String toString() {
        return "ParkingEntity{" +
                "mId=" + mId +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mPlaces=" + mPlaces +
                ", mAddress='" + mAddress + '\'' +
                ", mType='" + mType + '\'' +
                ", mPosition=" + mPosition +
                '}';
    }
}
