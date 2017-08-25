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
    private int serverId;

    @SerializedName("number")
    @ColumnInfo(name = "transport_number")
    private int number;

    @SerializedName("type")
    @ColumnInfo(name = "transport_type")
    @TypeConverters({TransportType.class})
    private TransportType type;

    @SerializedName("distance")
    @ColumnInfo(name = "transport_distance")
    private Double distance;

    @SerializedName("segments")
    @Ignore
    private List<SegmentEntity> segments;

    public TransportEntity(int serverId, int number, TransportType type, Double distance) {
        this.serverId = serverId;
        this.number = number;
        this.type = type;
        this.distance = distance;
    }

    public int getServerId() {
        return serverId;
    }

    public int getNumber() {
        return number;
    }

    public Double getDistance() {
        return distance;
    }

    public TransportType getType() {
        return type;
    }

    public List<SegmentEntity> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        return "TransportEntity{" +
                "serverId=" + serverId +
                ", number=" + number +
                ", type=" + type +
                ", distance=" + distance +
                ", segments=" + segments +
                '}';
    }
}
