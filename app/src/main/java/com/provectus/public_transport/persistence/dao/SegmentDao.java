package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.provectus.public_transport.persistence.entity.SegmentEntity;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Psihey on 20.08.2017.
 */

@Dao
public interface SegmentDao {

    @Insert
    void insertAll(List<SegmentEntity> segments);

    @Delete
    void delete(SegmentEntity segment);

    @Query("SELECT * FROM segments")
    Flowable<List<SegmentEntity>> getAllSegment();

    @Update()
    void updateSegment(List<SegmentEntity> segmentEntity);

}
