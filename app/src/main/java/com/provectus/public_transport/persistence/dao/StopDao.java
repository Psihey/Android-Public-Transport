package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import com.provectus.public_transport.model.StopEntity;

import java.util.List;


@Dao
public interface StopDao {

    @Insert
    void insertAll(List<StopEntity> stopping);

    @Delete()
    void deleteAll(List<StopEntity> stops);
}
