package com.provectus.public_transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Psihey on 18.08.2017.
 */

public class Segment {

    @SerializedName("direction")
    private int direction;;
    @SerializedName("position")
    private int position;
    @SerializedName("points")
    private List<Point> points;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
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
        return "SegmentEntity{" +
                "direction=" + direction +
                ", position=" + position +
                ", points=" + points +
                '}';
    }
}
