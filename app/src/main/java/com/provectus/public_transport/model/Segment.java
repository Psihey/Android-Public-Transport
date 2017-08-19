package com.provectus.public_transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Psihey on 18.08.2017.
 */

public class Segment {

    @SerializedName("id")
    private long id;
    @SerializedName("stoppingId")
    private long stoppingId;
    @SerializedName("routeId")
    private long routeId;
    @SerializedName("direction")
    private int direction;
    @SerializedName("built")
    private int built;
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lng")
    private double longitude;
    @SerializedName("position")
    private int position;
    @SerializedName("points")
    private List<Point> points;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStoppingId() {
        return stoppingId;
    }

    public void setStoppingId(long stoppingId) {
        this.stoppingId = stoppingId;
    }

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getBuilt() {
        return built;
    }

    public void setBuilt(int built) {
        this.built = built;
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

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "id=" + id +
                ", stoppingId=" + stoppingId +
                ", routeId=" + routeId +
                ", direction=" + direction +
                ", built=" + built +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", position=" + position +
                ", points=" + points +
                '}';
    }
}
