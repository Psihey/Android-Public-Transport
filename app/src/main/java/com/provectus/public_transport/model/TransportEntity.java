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

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "transport_id")
    private int mServerId;

    @SerializedName("number")
    @ColumnInfo(name = "transport_number")
    private int mNumber;

    @SerializedName("type")
    @ColumnInfo(name = "transport_type")
    @TypeConverters({TransportType.class})
    private TransportType mType;

    @SerializedName("distance")
    @ColumnInfo(name = "transport_distance")
    private Double mDistance;

    @SerializedName("segments")
    @Ignore
    private List<SegmentEntity> segments;

    public TransportEntity(int serverId, int number, TransportType type, Double distance) {
        mServerId = serverId;
        mNumber = number;
        mType = type;
        mDistance = distance;
    }

    public int getServerId() {
        return mServerId;
    }

    public int getNumber() {
        return mNumber;
    }

    public TransportType getType() {
        return mType;
    }

    public Double getDistance() {
        return mDistance;
    }

    public List<SegmentEntity> getSegments() {
        return segments;
    }


    @Override
    public String toString() {
        return "TransportEntity{" +
                "mServerId=" + mServerId +
                ", mNumber=" + mNumber +
                ", mType=" + mType +
                ", mDistance=" + mDistance +
                ", segments=" + segments +
                '}';
    }
}
