package com.provectus.public_transport.persistent.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.provectus.public_transport.model.Segment;
import com.provectus.public_transport.persistent.entity.SegmentEntity;

import java.util.List;

/**
 * Created by Psihey on 20.08.2017.
 */

@Dao
public interface SegmentDao {

    @Insert
    void insertAll(List<SegmentEntity> segments);

    @Delete
    void delete(SegmentEntity segment);

    @Query("SELECT * FROM segment")
    List<SegmentEntity> getAllTransport();

}
