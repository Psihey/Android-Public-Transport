package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.provectus.public_transport.model.SegmentWithPointsModel;
import com.provectus.public_transport.model.StopEntity;
import com.provectus.public_transport.model.TransportEntity;

import java.util.List;

import io.reactivex.Flowable;

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

    @Delete()
    void deleteAll(List<TransportEntity> transportEntities);

    @Query("SELECT * FROM transports "
            + "WHERE transports.transport_number = :transportNumber AND transports.transport_type = :transportType")
    Flowable<TransportEntity> getTransportEntity(int transportNumber, String transportType);

    @Query("SELECT * FROM transports "
            + "INNER JOIN segments ON segments.segment_transport_id = transports.transport_id "
            + "INNER JOIN points ON points.point_segment_id = segments.segment_id "
            + "WHERE transports.transport_number = :transportNumber AND transports.transport_type = :transportType")
    Flowable<List<SegmentWithPointsModel>> getSegmentForCurrentTransport(int transportNumber, String transportType);

    @Query("SELECT * FROM transports "
            + "INNER JOIN segments ON segments.segment_transport_id = transports.transport_id "
            + "INNER JOIN stopping ON stopping.stop_segment_id = segments.segment_id "
            + "WHERE transports.transport_number = :transportNumber AND transports.transport_type = :transportType")
    Flowable<List<StopEntity>> getStopsForCurrentTransport(int transportNumber, String transportType);

}
