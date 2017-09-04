package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.provectus.public_transport.model.PointEntity;

import java.util.List;

import io.reactivex.Flowable;


@Dao
public interface PointDao {

    @Insert
    void insertAll(List<PointEntity> points);

    @Query("SELECT * FROM points")
    Flowable<List<PointEntity>> getAllPoint();

    @Delete()
    void deleteAll(List<PointEntity> pointEntities);;

}
