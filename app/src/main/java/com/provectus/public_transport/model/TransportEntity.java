package com.provectus.public_transport.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Psihey on 11.08.2017.
 */
@Entity(tableName = "transports")
public class TransportEntity {

    @SerializedName("id")
    @ColumnInfo(name = "transport_id")
    @PrimaryKey()
    private long mServerId;

    @SerializedName("number")
    @ColumnInfo(name = "transport_number")
    private int mNumber;

    @SerializedName("type")
    @ColumnInfo(name = "transport_type")
    @TypeConverters({TransportType.class})
    private TransportType mType;

    @SerializedName("distance")
    @ColumnInfo(name = "transport_distance")
    private double mDistance;

    @ColumnInfo(name = "available")
    @TypeConverters({AvailableBooleanConverters.class})
    private boolean available;

    @SerializedName("segments")
    @Ignore
    private List<SegmentEntity> mSegments;

    @Ignore
    private boolean mIsSelected;

    public TransportEntity(long serverId, int number, TransportType type, double distance, boolean available) {
        mServerId = serverId;
        mNumber = number;
        mType = type;
        mDistance = distance;
        this.available = available;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public long getServerId() {
        return mServerId;
    }

    public boolean isIsSelected() {
        return mIsSelected;
    }

    public void setIsSelected(boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }

    public int getNumber() {
        return mNumber;
    }

    public TransportType getType() {
        return mType;
    }

    public double getDistance() {
        return mDistance;
    }

    public List<SegmentEntity> getSegments() {
        return mSegments;
    }


    @Override
    public String toString() {
        return "TransportEntity{" +
                "ServerId=" + mServerId +
                ", Number=" + mNumber +
                ", Type=" + mType +
                ", Distance=" + mDistance +
                ", segments=" + mSegments +
                '}';
    }
}
