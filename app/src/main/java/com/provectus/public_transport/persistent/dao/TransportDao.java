package com.provectus.public_transport.persistent.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.provectus.public_transport.persistent.entity.TransportEntity;

import java.util.List;

/**
 * Created by Psihey on 20.08.2017.
 */

@Dao
public interface TransportDao {

    @Insert
    void insertAll(List<TransportEntity> transports);

    @Delete
    void delete(TransportEntity transport);

    @Query("SELECT * FROM transport")
    List<TransportEntity> getAllTransport();

}
