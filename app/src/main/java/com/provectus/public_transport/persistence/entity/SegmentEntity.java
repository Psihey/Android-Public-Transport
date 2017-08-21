package com.provectus.public_transport.persistence.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Psihey on 20.08.2017.
 */

@Entity(tableName = "segments",
        foreignKeys = @ForeignKey(entity = TransportEntity.class,
                                  parentColumns = "id",
                                  childColumns = "transport_id"))
public class SegmentEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "segment_position")
    private int position;
    @ColumnInfo(name = "segment_direction")
    private int direction;
    @ColumnInfo(name = "transport_id")
    private long transportId;

    public SegmentEntity(int position, int direction, long transportId) {
        this.position = position;
        this.direction = direction;
        this.transportId = transportId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public long getTransportId() {
        return transportId;
    }

    public void setTransportId(long transportId) {
        this.transportId = transportId;
    }

    @Override
    public String toString() {
        return "SegmentEntity{" +
                "id=" + id +
                ", position=" + position +
                ", direction=" + direction +
                ", transportId=" + transportId +
                '}';
    }
}
