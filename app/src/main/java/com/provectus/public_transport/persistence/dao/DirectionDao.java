package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import com.provectus.public_transport.model.DirectEntity;

import java.util.List;


@Dao
public interface DirectionDao {
    @Insert
    void insertAll(List<DirectEntity> directions);

    @Delete()
    void deleteAll(List<DirectEntity> directions);
}
