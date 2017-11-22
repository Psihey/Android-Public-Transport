package com.provectus.public_transport.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = "stopping",
        foreignKeys = @ForeignKey(entity = SegmentEntity.class,
                parentColumns = "segment_id",
                childColumns = "stop_segment_id",
                onDelete = ForeignKey.CASCADE), indices = {@Index(value = {"stop_segment_id"})})
public class StopEntity {

    @PrimaryKey(autoGenerate = true)
    private long mId;

    @SerializedName("id")
    @ColumnInfo(name = "stop_id")
    private long mServerId;

    @SerializedName("lat")
    @ColumnInfo(name = "latitude")
    private double mLatitude;

    @SerializedName("lng")
    @ColumnInfo(name = "longitude")
    private double mLongitude;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "stop_segment_id")
    private long mSegmentId;

    public StopEntity(long mServerId, double mLatitude, double mLongitude, String mTitle, long mSegmentId) {
        this.mServerId = mServerId;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mTitle = mTitle;
        this.mSegmentId = mSegmentId;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getServerId() {
        return mServerId;
    }

    public void setServerId(long mServerId) {
        this.mServerId = mServerId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public long getSegmentId() {
        return mSegmentId;
    }

    public void setSegmentId(long mSegmentId) {
        this.mSegmentId = mSegmentId;
    }

    @Override
    public String toString() {
        return "StopEntity{" +
                "Id=" + mServerId +
                ", Latitude=" + mLatitude +
                ", Longitude=" + mLongitude +
                ", Title='" + mTitle + '\'' +
                ", SegmentId=" + mSegmentId +
                '}';
    }
}
