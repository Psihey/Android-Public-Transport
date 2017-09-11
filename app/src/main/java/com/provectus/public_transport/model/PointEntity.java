package com.provectus.public_transport.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import static android.R.attr.id;

@Entity(tableName = "points",
        foreignKeys = @ForeignKey(entity = SegmentEntity.class,
                parentColumns = "segment_id",
                childColumns = "point_segment_id",
                onDelete = ForeignKey.CASCADE),indices = {@Index(value = {"point_segment_id"})})

public class PointEntity {

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

    @ColumnInfo(name = "point_segment_id")
    private long mSegmentId;

    public PointEntity(double latitude, double longitude, int position, long segmentId) {
        mLatitude = latitude;
        mLongitude = longitude;
        mPosition = position;
        mSegmentId = segmentId;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
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
