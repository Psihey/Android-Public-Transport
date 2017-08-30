package com.provectus.public_transport.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Psihey on 11.08.2017.
 */
@Entity(tableName = "transports",indices = {@Index(value = {"transport_id"},unique = true)})
public class TransportEntity {

    @PrimaryKey(autoGenerate = true)
    private long mId;

    @SerializedName("id")
    @ColumnInfo(name = "transport_id")
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

    @SerializedName("segments")
    @Ignore
    private List<SegmentEntity> segments;

    public TransportEntity(long serverId, int number, TransportType type, double distance) {
        mServerId = serverId;
        mNumber = number;
        mType = type;
        mDistance = distance;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getServerId() {
        return mServerId;
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
        return segments;
    }


    @Override
    public String toString() {
        return "TransportEntity{" +
                "ServerId=" + mServerId +
                ", Number=" + mNumber +
                ", Type=" + mType +
                ", Distance=" + mDistance +
                ", segments=" + segments +
                '}';
    }
}
