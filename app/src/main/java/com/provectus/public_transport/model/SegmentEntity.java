package com.provectus.public_transport.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Psihey on 18.08.2017.
 */
@Entity(tableName = "segments",
        foreignKeys = @ForeignKey(entity = TransportEntity.class,
                parentColumns = "transport_id",
                childColumns = "transport_id"))
public class SegmentEntity  {

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private int serverId;

    @SerializedName("direction")
    @ColumnInfo(name = "segment_direction")
    private int direction;

    @SerializedName("position")
    @ColumnInfo(name = "segment_position")
    private int position;

    @ColumnInfo(name = "transport_id")
    private long transportId;

    @SerializedName("points")
    @Ignore
    private List<PointEntity> points;

    public SegmentEntity(int serverId, int direction, int position, long transportId) {
        this.serverId = serverId;
        this.direction = direction;
        this.position = position;
        this.transportId = transportId;
    }

    public int getServerId() {
        return serverId;
    }

    public int getDirection() {
        return direction;
    }

    public int getPosition() {
        return position;
    }

    public long getTransportId() {
        return transportId;
    }

    public List<PointEntity> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "SegmentEntity{" +
                "serverId=" + serverId +
                ", direction=" + direction +
                ", position=" + position +
                ", transportId=" + transportId +
                ", points=" + points +
                '}';
    }
}
