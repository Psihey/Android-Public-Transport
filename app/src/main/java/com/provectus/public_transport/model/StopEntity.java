package com.provectus.public_transport.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Psihey on 28.08.2017.
 */

@Entity(tableName = "stopping",
        foreignKeys = @ForeignKey(entity = SegmentEntity.class,
                parentColumns = "segment_id",
                childColumns = "segment_id"))
public class StopEntity {

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @SerializedName("lat")
    @ColumnInfo(name = "latitude")
    private double mLatitude;

    @SerializedName("lng")
    @ColumnInfo(name = "longitude")
    private double mLongitude;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "segment_id")
    private long mSegmentId;

    public StopEntity(double mLatitude, double mLongitude, String mTitle, long mSegmentId) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mTitle = mTitle;
        this.mSegmentId = mSegmentId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getId() {
        return mId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getTitle() {
        return mTitle;
    }

    public long getSegmentId() {
        return mSegmentId;
    }

    @Override
    public String toString() {
        return "StopEntity{" +
                "Id=" + mId +
                ", Latitude=" + mLatitude +
                ", Longitude=" + mLongitude +
                ", Title='" + mTitle + '\'' +
                ", SegmentId=" + mSegmentId +
                '}';
    }
}
