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
public class SegmentEntity {

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private int mServerId;

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

    public SegmentEntity(int serverId, int direction, int position, long transportId) {
        mServerId = serverId;
        mDirection = direction;
        mPosition = position;
        mTransportId = transportId;
    }


    public int getmServerId() {
        return mServerId;
    }

    public int getmDirection() {
        return mDirection;
    }

    public int getmPosition() {
        return mPosition;
    }

    public long getmTransportId() {
        return mTransportId;
    }

    public List<PointEntity> getmPoints() {
        return mPoints;
    }

    @Override
    public String toString() {
        return "SegmentEntity{" +
                "mServerId=" + mServerId +
                ", mDirection=" + mDirection +
                ", mPosition=" + mPosition +
                ", mTransportId=" + mTransportId +
                ", mPoints=" + mPoints +
                '}';
    }
}
