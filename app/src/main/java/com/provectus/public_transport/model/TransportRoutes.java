package com.provectus.public_transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    private Double distance;
    @SerializedName("segments")
    private List<Segment> segment;

    public List<Segment> getSegment() {
        return segment;
    }

    public void setSegment(List<Segment> segment) {
        this.segment = segment;
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

    public TransportType getType() {
        return type;
    }

    public void setType(TransportType type) {
        this.type = type;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "TransportRoutes{" +
                "id=" + id +
                ", number=" + number +
                ", type=" + type +
                ", distance=" + distance +
                ", segment=" + segment +
                '}';
    }
}
