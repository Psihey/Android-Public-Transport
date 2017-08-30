package com.provectus.public_transport.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Psihey on 18.08.2017.
 */
@Entity(tableName = "segments",
        foreignKeys = @ForeignKey(entity = TransportEntity.class,
                parentColumns = "transport_id",
                childColumns = "transport_id"), indices = {@Index(value = {"segment_id"}, unique = true)})
public class SegmentEntity {

    @PrimaryKey(autoGenerate = true)
    private long mId;

    @SerializedName("id")
    @ColumnInfo(name = "segment_id")
    private long mServerId;

    @SerializedName("direction")
    @ColumnInfo(name = "segment_direction")
    private int mDirection;

    @SerializedName("position")
    @ColumnInfo(name = "segment_position")
    private int mPosition;

    @ColumnInfo(name = "transport_id")
    private long mTransportId;

    @SerializedName("points")
    @Ignore
    private List<PointEntity> mPoints;

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

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
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

    @Override
    public String toString() {
        return "SegmentEntity{" +
                "ServerId=" + mServerId +
                ", Direction=" + mDirection +
                ", Position=" + mPosition +
                ", TransportId=" + mTransportId +
                ", Points=" + mPoints +
                '}';
    }
}
