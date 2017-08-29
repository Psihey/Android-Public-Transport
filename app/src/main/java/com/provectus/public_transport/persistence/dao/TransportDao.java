package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.provectus.public_transport.model.PointEntity;
import com.provectus.public_transport.model.SegmentEntity;
import com.provectus.public_transport.model.StopEntity;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.TransportType;

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

    @Query("SELECT * FROM transports "
            + "INNER JOIN segments ON segments.transport_id = transports.transport_id "
            + "INNER JOIN points ON points.segment_id = segments.segment_id "
            + "WHERE transports.transport_number = :tramNumber AND transports.transport_type = :type")
    Flowable<List<SegmentEntity>> getSegmentForCurrentTrolley(int tramNumber, String type);

    @Query("SELECT * FROM transports "
            + "INNER JOIN segments ON segments.transport_id = transports.transport_id "
            + "INNER JOIN points ON points.segment_id = segments.segment_id "
            + "WHERE transports.transport_number = :tramNumber AND transports.transport_type = :type")
    Flowable<List<PointEntity>> getPointsForCurrentTrolley(int tramNumber, String type);

    @Query("SELECT * FROM transports "
            + "INNER JOIN segments ON segments.transport_id = transports.transport_id "
            + "INNER JOIN stopping ON stopping.segment_id = segments.segment_id "
            + "WHERE transports.transport_number = :tramNumber AND transports.transport_type = :type")
    Flowable<List<StopEntity>> getStopsForCurrentTrolley(int tramNumber, String type);

}
