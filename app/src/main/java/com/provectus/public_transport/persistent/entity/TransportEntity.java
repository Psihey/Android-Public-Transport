package com.provectus.public_transport.persistent.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Psihey on 20.08.2017.
 */

@Entity(tableName = "transport")
public class TransportEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    private long id;
    @ColumnInfo(name = "transport_number")
    private int number;
    @ColumnInfo(name = "transport_type")
    @NonNull
    private String type;
    @ColumnInfo(name = "transport_distance")
    private String distance;

    public TransportEntity(long id, int number, @NonNull String type, String distance) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.distance = distance;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "TransportEntity{" +
                "id=" + id +
                ", number=" + number +
                ", type='" + type + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
