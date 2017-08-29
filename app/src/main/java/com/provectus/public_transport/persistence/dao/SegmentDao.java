package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.provectus.public_transport.model.SegmentEntity;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Psihey on 20.08.2017.
 */

@Dao
public interface SegmentDao {

    @Insert
    void insertAll(List<SegmentEntity> segments);

    @Query("SELECT * FROM segments")
    Flowable<List<SegmentEntity>> getAllSegment();

    @Query("DELETE FROM segments")
    void deleteAll();

}
