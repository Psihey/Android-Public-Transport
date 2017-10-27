package com.provectus.public_transport.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.provectus.public_transport.model.converter.TransportType;

@Entity(tableName = "stoppingDetail")
public class StopDetailEntity {

    @PrimaryKey(autoGenerate = true)
    private long mId;

    @ColumnInfo(name = "stop_detail_stop_id")
    private long mStopsId;

    @ColumnInfo(name = "firstStopping")
    @SerializedName("firstStopping")
    private String mFirstStopping;

    @ColumnInfo(name = "lastStopping")
    @SerializedName("lastStopping")
    private String mLastStopping;

    @ColumnInfo(name = "number")
    @SerializedName("number")
    private int mNumber;

    @ColumnInfo(name = "transport_type")
    @TypeConverters({TransportType.class})
    @SerializedName("type")
    private TransportType mTransportType;

    public StopDetailEntity(long mStopsId, String mFirstStopping, String mLastStopping, int mNumber, TransportType mTransportType) {
        this.mStopsId = mStopsId;
        this.mFirstStopping = mFirstStopping;
        this.mLastStopping = mLastStopping;
        this.mNumber = mNumber;
        this.mTransportType = mTransportType;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getId() {
        return mId;
    }

    public long getStopsId() {
        return mStopsId;
    }

    public String getFirstStopping() {
        return mFirstStopping;
    }

    public String getLastStopping() {
        return mLastStopping;
    }

    public int getNumber() {
        return mNumber;
    }

    public TransportType getTransportType() {
        return mTransportType;
    }

    @Override
    public String toString() {
        return "StopDetailEntity{" +
                "mStopsId=" + mStopsId +
                ", mFirstStopping='" + mFirstStopping + '\'' +
                ", mLastStopping='" + mLastStopping + '\'' +
                ", mNumber=" + mNumber +
                ", mTransportType=" + mTransportType +
                '}';
    }
}
