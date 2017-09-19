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
@Entity(tableName = "indirection",
        foreignKeys = @ForeignKey(entity = TransportEntity.class,
                parentColumns = "transport_id",
                childColumns = "indirection_transport_id",
                onDelete = ForeignKey.CASCADE),indices = {@Index(value = {"indirection_transport_id"})})
public class IndirectionEntity {

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

    @ColumnInfo(name = "indirection_transport_id")
    private long mTransportId;

    public IndirectionEntity(double mLatitude, double mLongitude, int mPosition, long mTransportId) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mPosition = mPosition;
        this.mTransportId = mTransportId;
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
        return "IndirectionEntity{" +
                "mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mPosition=" + mPosition +
                '}';
    }
}
