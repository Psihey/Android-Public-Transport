package com.provectus.public_transport.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "segments",
        foreignKeys = @ForeignKey(entity = TransportEntity.class,
                parentColumns = "transport_id",
                childColumns = "segment_transport_id",
                onDelete = ForeignKey.CASCADE),indices = {@Index(value = {"segment_transport_id"})})
public class SegmentEntity {

    @SerializedName("id")
    @ColumnInfo(name = "segment_id")
    @PrimaryKey()
    private long mServerId;

    @SerializedName("direction")
    @ColumnInfo(name = "segment_direction")
    private int mDirection;

    @SerializedName("position")
    @ColumnInfo(name = "segment_position")
    private int mPosition;

    @ColumnInfo(name = "segment_transport_id")
    private long mTransportId;

    @SerializedName("points")
    @Ignore
    private List<PointEntity> mPoints;

    @Embedded
    @Ignore
    private PointEntity pointEntity;

    @SerializedName("stoppingId")
    @Ignore
    private int stoppingId;

    @SerializedName("stopping")
    @Ignore
    private StopEntity mStopEntity;


    public SegmentEntity(long serverId, int direction, int position, long transportId) {
        mServerId = serverId;
        mDirection = direction;
        mPosition = position;
        mTransportId = transportId;
    }

    public SegmentEntity(int Direction, int Position, List<PointEntity> Points) {
        this.mDirection = Direction;
        this.mPosition = Position;
        this.mPoints = Points;
    }

    public long getServerId() {
        return mServerId;
    }

    public int getDirection() {
        return mDirection;
    }

    public int getPosition() {
        return mPosition;
    }

    public long getTransportId() {
        return mTransportId;
    }

    public List<PointEntity> getPoints() {
        return mPoints;
    }

    public StopEntity getStopEntity() {
        return mStopEntity;
    }

    public PointEntity getPointEntity() {
        return pointEntity;
    }

    @Override
    public String toString() {
        return "SegmentEntity{" +
                "ServerId=" + mServerId +
                ", Direction=" + mDirection +
                ", Position=" + mPosition +
                ", TransportId=" + mTransportId +
                ", Points=" + pointEntity +
                '}';
    }
}
