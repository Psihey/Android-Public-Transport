package com.provectus.public_transport.persistence.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.provectus.public_transport.model.StopDetailEntity;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface StopDetailDao {

    @Insert
    void insertAll(List<StopDetailEntity> stopDetails);

    @Delete
    void deleteAll(List<StopDetailEntity> stopDetails);

    @Query("SELECT * FROM stoppingDetail ")
    Maybe<List<StopDetailEntity>> getAllStopDetail();

    @Query("SELECT * FROM stoppingDetail "
            + "WHERE stoppingDetail.stop_detail_stop_id = :stopId")
    Maybe<List<StopDetailEntity>> getStopDetail(long stopId);
}
