package com.provectus.public_transport.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Psihey on 19.09.2017.
 */
@Entity(tableName = "direction",
        foreignKeys = @ForeignKey(entity = TransportEntity.class,
                parentColumns = "transport_id",
                childColumns = "direction_transport_id",
                onDelete = ForeignKey.CASCADE),indices = {@Index(value = {"direction_transport_id"})})
public class DirectEntity {

    @PrimaryKey(autoGenerate = true)
    private long mId;

    @SerializedName("lat")
    @ColumnInfo(name = "latitude")
    private double mLatitude;

    @SerializedName("lng")
    @ColumnInfo(name = "longitude")
    private double mLongitude;

    @SerializedName("position")
    @ColumnInfo(name = "point_position")
    private int mPosition;

    @ColumnInfo(name = "direction_transport_id")
    private long mTransportId;

    public DirectEntity(double latitude, double longitude, int position, long transportId) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mPosition = position;
        this.mTransportId = transportId;
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

    public int getPosition() {
        return mPosition;
    }

    public long getTransportId() {
        return mTransportId;
    }

    @Override
    public String toString() {
        return "DirectionDao{" +
                "mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mPosition=" + mPosition +
                '}';
    }
}
