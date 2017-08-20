package com.provectus.public_transport.persistent.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Psihey on 20.08.2017.
 */

@Entity(tableName = "point",
        foreignKeys =@ForeignKey(entity = SegmentEntity.class,
                                 parentColumns = "id",
                                 childColumns = "segment_id"))
public class PointEntity  {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "point_position")
    private int position;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "segment_id")
    private long segmentId;

    public PointEntity( double latitude, double longitude, long segmentId) {
        this.position = position;
        this.latitude = latitude;
        this.longitude = longitude;
        this.segmentId = segmentId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(long segmentId) {
        this.segmentId = segmentId;
    }

    @Override
    public String toString() {
        return "PointEntity{" +
                "id=" + id +
                ", position=" + position +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", segmentId=" + segmentId +
                '}';
    }
}
