package com.provectus.public_transport.model;

import java.util.Set;

/**
 * Created by Psihey on 05.10.2017.
 */

public class VehicleMarkerInfoModel {

    private long mVehicleId;

    private int mNumber;

    private double mDistance;

    private long mServerId;

    private Set<String> mStopsName;

    public VehicleMarkerInfoModel(long mVehicleId, int mNumber, double mDistance, long mServerId, Set<String> mStopsName) {
        this.mVehicleId = mVehicleId;
        this.mNumber = mNumber;
        this.mDistance = mDistance;
        this.mServerId = mServerId;
        this.mStopsName = mStopsName;
    }

    public long getServerId() {
        return mServerId;
    }

    public void setServerId(long mServerId) {
        this.mServerId = mServerId;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int mNumber) {
        this.mNumber = mNumber;
    }

    public VehicleMarkerInfoModel() {
    }

    public long getVehicleId() {
        return mVehicleId;
    }

    public double getDistance() {
        return mDistance;
    }

    public Set<String> getStopsName() {
        return mStopsName;
    }

    public void setVehicleId(long mVehicleId) {
        this.mVehicleId = mVehicleId;
    }

    public void setDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public void setStopsName(Set<String> mStopsName) {
        this.mStopsName = mStopsName;
    }

    @Override
    public String toString() {
        return "VehicleMarkerInfoModel{" +
                "mVehicleId=" + mVehicleId +
                ", mNumber=" + mNumber +
                ", mDistance=" + mDistance +
                ", mStopsName=" + mStopsName +
                '}';
    }
}
