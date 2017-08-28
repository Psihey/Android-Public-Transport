package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.provectus.public_transport.model.StopEntity;

import java.util.List;

/**
 * Created by Psihey on 28.08.2017.
 */

@Dao
public interface StopDao {

    @Insert
    void insertAll(List<StopEntity> stopping);

    @Query("DELETE FROM stopping")
    void deleteAll();

}
