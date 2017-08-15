package com.provectus.public_transport.model;

import com.google.gson.annotations.SerializedName;
import com.provectus.public_transport.view.adapter.TramsAndTrolleyAdapter;

import java.util.Objects;

/**
 * Created by Psihey on 11.08.2017.
 */

public class TransportRoutes {

    @SerializedName("id")
    private long id;
    @SerializedName("number")
    private int number;
    @SerializedName("type")
    private TransportType type;
    @SerializedName("distance")
    private String distance;

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

    public TransportType getType() {
        return type;
    }

    public void setType(TransportType type) {
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
                ", type=" + type +
                ", distance='" + distance + '\'' +
                '}';
    }
}
