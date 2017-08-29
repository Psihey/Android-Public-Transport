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
    private String mLatitude;

    @SerializedName("lng")
    @ColumnInfo(name = "longitude")
    private String mLongitude;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "segment_id")
    private long mSegmentId;

    public StopEntity(String latitude, String longitude, String title, long segmentId) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mTitle = title;
        this.mSegmentId = segmentId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getId() {
        return mId;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public String getLongitude() {
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
