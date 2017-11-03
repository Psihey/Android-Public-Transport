package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import com.provectus.public_transport.model.SegmentEntity;

import java.util.List;


@Dao
public interface SegmentDao {

    @Insert
    void insertAll(List<SegmentEntity> segments);

    @Delete()
    void deleteAll(List<SegmentEntity> segments);

}
