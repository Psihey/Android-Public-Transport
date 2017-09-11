package com.provectus.public_transport.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;


public class SegmentWithPointsModel {

    @Embedded
    private
    SegmentEntity mSegmentEntity;

    @Relation(parentColumn = "segment_id", entityColumn = "point_segment_id")
    private
    List<PointEntity> mPointEntities;

    public SegmentEntity getSegmentEntity() {
        return mSegmentEntity;
    }

    public List<PointEntity> getPointEntities() {
        return mPointEntities;
    }

    public void setSegmentEntity(SegmentEntity mSegmentEntity) {
        this.mSegmentEntity = mSegmentEntity;
    }

    public void setPointEntities(List<PointEntity> mPointEntities) {
        this.mPointEntities = mPointEntities;
    }

    @Override
    public String toString() {
        return "SegmentWithPointsModel{" +
                "mSegmentEntity=" + mSegmentEntity +
                ", mPointEntities=" + mPointEntities +
                '}';
    }
}
