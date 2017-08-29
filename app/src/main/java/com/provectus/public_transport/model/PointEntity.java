package com.provectus.public_transport.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import static android.R.attr.id;

/**
 * Created by Psihey on 18.08.2017.
 */
@Entity(tableName = "points",
        foreignKeys = @ForeignKey(entity = SegmentEntity.class,
                parentColumns = "segment_id",
                childColumns = "segment_id"))

public class PointEntity {

    @PrimaryKey(autoGenerate = true)
    private int mId;

    @SerializedName("lat")
    @ColumnInfo(name = "latitude")
    private double mLatitude;

    @SerializedName("lng")
    @ColumnInfo(name = "longitude")
    private double mLongitude;

    @SerializedName("position")
    @ColumnInfo(name = "point_position")
    private int mPosition;

    @ColumnInfo(name = "segment_id")
    private long mSegmentId;

    public PointEntity(double latitude, double longitude, int position, long segmentId) {
        mLatitude = latitude;
        mLongitude = longitude;
        mPosition = position;
        mSegmentId = segmentId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
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

    public long getSegmentId() {
        return mSegmentId;
    }

    @Override
    public String toString() {
        return "PointEntity{" +
                "id=" + id +
                ", Latitude=" + mLatitude +
                ", Longitude=" + mLongitude +
                ", Position=" + mPosition +
                ", SegmentId=" + mSegmentId +
                '}';
    }
}
