package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.provectus.public_transport.persistence.entity.PointEntity;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Psihey on 20.08.2017.
 */

@Dao
public interface PointDao {

    @Insert
    void insertAll(List<PointEntity> points);

    @Delete
    void delete(PointEntity point);

    @Query("SELECT * FROM points")
    Flowable<List<PointEntity>> getAllPoint();

    @Update()
    void updatePoint(List<PointEntity> pointEntity);

}
