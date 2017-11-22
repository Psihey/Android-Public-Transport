package com.provectus.public_transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoppingsModel {

    @SerializedName("id")
    private long mStoppingID;

    @SerializedName("routes")
    private List<StopDetailEntity> mStopDetail;

    public StoppingsModel(long mStoppingID) {
        this.mStoppingID = mStoppingID;
    }

    public long getStoppingID() {
        return mStoppingID;
    }

    public List<StopDetailEntity> getStopDetail() {
        return mStopDetail;
    }


}
