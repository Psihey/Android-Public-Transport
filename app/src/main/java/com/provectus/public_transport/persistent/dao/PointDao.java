package com.provectus.public_transport.persistent.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.provectus.public_transport.persistent.entity.PointEntity;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Psihey on 20.08.2017.
 */

@Dao
public interface PointDao {

    @Insert
    void insertAll(List<PointEntity> segments);

    @Delete
    void delete(PointEntity point);

    @Query("SELECT * FROM point")
    Flowable<List<PointEntity>> getAllTransport();

}
