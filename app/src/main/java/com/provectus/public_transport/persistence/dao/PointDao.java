package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import com.provectus.public_transport.model.PointEntity;

import java.util.List;


@Dao
public interface PointDao {

    @Insert
    void insertAll(List<PointEntity> points);

    @Delete()
    void deleteAll(List<PointEntity> points);

}
