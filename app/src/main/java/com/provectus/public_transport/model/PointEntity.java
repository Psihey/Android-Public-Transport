package com.provectus.public_transport.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Psihey on 18.08.2017.
 */
@Entity(tableName = "points",
        foreignKeys = @ForeignKey(entity = SegmentEntity.class,
                parentColumns = "id",
                childColumns = "segment_id"))
public class PointEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("lat")
    @ColumnInfo(name = "latitude")
    private double latitude;

    @SerializedName("lng")
    @ColumnInfo(name = "longitude")
    private double longitude;

    @SerializedName("position")
    @ColumnInfo(name = "point_position")
    private int position;

    @ColumnInfo(name = "segment_id")
    private long segmentId;

    public PointEntity(double latitude, double longitude, int position, long segmentId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.position = position;
        this.segmentId = segmentId;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getPosition() {
        return position;
    }

    public long getSegmentId() {
        return segmentId;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PointEntity{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", position=" + position +
                ", segmentId=" + segmentId +
                '}';
    }
}
