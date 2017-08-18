package com.provectus.public_transport.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Psihey on 18.08.2017.
 */

public class Point {
    @SerializedName("id")
    private long id;

    @SerializedName("segmentId")
    private long segmentId;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lng")
    private double longitude;

    @SerializedName("position")
    private int position;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(long segmentId) {
        this.segmentId = segmentId;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", segmentId=" + segmentId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", position=" + position +
                '}';
    }
}
