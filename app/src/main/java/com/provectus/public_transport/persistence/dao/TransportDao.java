package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Delete
    void delete(TransportEntity transport);

    @Query("SELECT * FROM transports")
    Flowable<List<TransportEntity>> getAllTransport();

    @Update()
    void updateTransport(List<TransportEntity> transportEntity);

    @Query("SELECT * FROM transports WHERE transport_type = 'TRAM_TYPE'")
    Flowable<List<TransportEntity>> getAllTram();

    @Query("SELECT * FROM transports WHERE transport_type = 'TROLLEYBUSES_TYPE'")
    Flowable<List<TransportEntity>> getAllTrolleybuses();

    @Query("SELECT * FROM transports "
            + "INNER JOIN segments ON segments.transport_id = transports.transport_id "
            + "WHERE transports.transport_number = 28 AND transports.transport_type = 'TRAM_TYPE'")
    Flowable <List<TransportEntity>> get28Tram();

}
