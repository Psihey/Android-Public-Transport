package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.provectus.public_transport.model.StopEntity;

import java.util.List;


@Dao
public interface StopDao {

    @Insert
    void insertAll(List<StopEntity> stopping);

    @Query("DELETE FROM stopping")
    void deleteAll();

    @Delete()
    void deleteAll(List<StopEntity> stopEntities);
}
