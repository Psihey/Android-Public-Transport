package com.provectus.public_transport.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Psihey on 11.08.2017.
 */

public class TransportRoutes {

    @SerializedName("id")
    private long id;
    @SerializedName("number")
    private int number;
    @SerializedName("type")
    private String type;
    @SerializedName("distance")
    private String distance;

    public TransportRoutes(int number, String type) {
        this.number = number;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "TransportRoutes{" +
                "id=" + id +
                ", number=" + number +
                ", type='" + type + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
