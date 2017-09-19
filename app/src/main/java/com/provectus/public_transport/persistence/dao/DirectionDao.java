package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import com.provectus.public_transport.model.DirectionEntity;

import java.util.List;

/**
 * Created by Psihey on 19.09.2017.
 */
@Dao
public interface DirectionDao {
    @Insert
    void insertAll(List<DirectionEntity> direction);

    @Delete()
    void deleteAll(List<DirectionEntity> directionEntities);
}
