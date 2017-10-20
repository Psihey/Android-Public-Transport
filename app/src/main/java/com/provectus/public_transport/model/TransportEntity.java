package com.provectus.public_transport.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.provectus.public_transport.model.converter.BooleanConverter;
import com.provectus.public_transport.model.converter.TransportType;

import java.util.List;

@Entity(tableName = "transports", indices = {@Index(value = {"transport_id"})})
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
    @TypeConverters({BooleanConverter.class})
    private boolean mIsAvailable;

    @ColumnInfo(name = "favourites")
    @TypeConverters({BooleanConverter.class})
    private boolean mIsFavourites;

    @SerializedName("cost")
    @ColumnInfo(name = "cost")
    private double mCost;

    @SerializedName("firstStop")
    @ColumnInfo(name = "firstStop")
    private String mFirstStop;

    @SerializedName("lastStop")
    @ColumnInfo(name = "lastStop")
    private String mLastStop;

    @SerializedName("segments")
    @Ignore
    private List<SegmentEntity> mSegments;

    @SerializedName("direction0")
    @Ignore
    private List<DirectEntity> mDirectionEntity;

    @SerializedName("direction1")
    @Ignore
    private List<IndirectionModel> mIndirectionEntity;

    @Ignore
    private boolean mIsSelected;

    public TransportEntity(long mServerId,
                           int mNumber,
                           TransportType mType,
                           double mDistance,
                           boolean mIsAvailable,
                           boolean mIsFavourites,
                           double cost,
                           String firstStop,
                           String lastStop) {
        this.mServerId = mServerId;
        this.mNumber = mNumber;
        this.mType = mType;
        this.mDistance = mDistance;
        this.mIsAvailable = mIsAvailable;
        this.mIsFavourites = mIsFavourites;
        this.mCost = cost;
        this.mFirstStop = firstStop;
        this.mLastStop = lastStop;
    }

    @Ignore
    public TransportEntity(int mNumber) {
        this.mNumber = mNumber;
    }

    public boolean isAvailable() {
        return mIsAvailable;
    }

    public void setIsAvailable(boolean mIsAvailable) {
        this.mIsAvailable = mIsAvailable;
    }

    public long getServerId() {
        return mServerId;
    }

    public boolean isSelected() {
        return mIsSelected;
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

    public void setServerId(long mServerId) {
        this.mServerId = mServerId;
    }

    public void setNumber(int mNumber) {
        this.mNumber = mNumber;
    }

    public void setType(TransportType mType) {
        this.mType = mType;
    }

    public void setDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public void setIsSelected(boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }

    public List<DirectEntity> getDirectionEntity() {
        return mDirectionEntity;
    }

    public List<IndirectionModel> getIndirectionEntity() {
        return mIndirectionEntity;
    }

    public boolean isFavourites() {
        return mIsFavourites;
    }

    public void setIsFavourites(boolean mIsFavourites) {
        this.mIsFavourites = mIsFavourites;
    }

    public double getCost() {
        return mCost;
    }

    public String getFirstStop() {
        return mFirstStop;
    }

    public String getLastStop() {
        return mLastStop;
    }

    @Override
    public String toString() {
        return "TransportEntity{" +
                "mServerId=" + mServerId +
                ", mNumber=" + mNumber +
                ", mType=" + mType +
                ", mDistance=" + mDistance +
                ", mIsAvailable=" + mIsAvailable +
                ", mIsFavourites=" + mIsFavourites +
                ", mCost=" + mCost +
                ", mFirstStop='" + mFirstStop + '\'' +
                ", mLastStop='" + mLastStop + '\'' +
                ", mSegments=" + mSegments +
                ", mDirectionEntity=" + mDirectionEntity +
                ", mIndirectionEntity=" + mIndirectionEntity +
                ", mIsSelected=" + mIsSelected +
                '}';
    }
}
