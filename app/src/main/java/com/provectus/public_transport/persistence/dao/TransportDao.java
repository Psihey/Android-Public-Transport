package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.provectus.public_transport.model.TransportEntity;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Psihey on 20.08.2017.
 */

@Dao
public interface TransportDao {

    @Insert
    void insertAll(List<TransportEntity> transports);

    @Query("SELECT * FROM transports")
    Flowable<List<TransportEntity>> getAllTransport();

    @Query("SELECT * FROM transports WHERE transport_type = 'TRAM_TYPE'")
    Flowable<List<TransportEntity>> getAllTram();

    @Query("SELECT * FROM transports WHERE transport_type = 'TROLLEYBUSES_TYPE'")
    Flowable<List<TransportEntity>> getAllTrolleybuses();

    @Query("DELETE FROM segments")
    void deleteAll();

}
