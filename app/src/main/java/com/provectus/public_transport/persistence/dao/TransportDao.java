package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.provectus.public_transport.model.DirectEntity;
import com.provectus.public_transport.model.StopEntity;
import com.provectus.public_transport.model.TransportEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface TransportDao {

    @Insert
    void insertAll(List<TransportEntity> transports);

    @Delete
    void deleteAll(List<TransportEntity> transports);

    @Query("SELECT * FROM transports WHERE transport_type = 'TRAM_TYPE'")
    Maybe<List<TransportEntity>> getAllTram();

    @Query("SELECT * FROM transports WHERE transport_type = 'TROLLEYBUSES_TYPE'")
    Maybe<List<TransportEntity>> getAllTrolleybuses();

    @Query("SELECT * FROM transports "
            + "WHERE transports.transport_number = :transportNumber AND transports.transport_type = :transportType")
    Maybe<TransportEntity> getTransportEntity(int transportNumber, String transportType);

    @Query("SELECT * FROM transports "
            + "INNER JOIN segments ON segments.segment_transport_id = transports.transport_id "
            + "INNER JOIN stopping ON stopping.stop_segment_id = segments.segment_id "
            + "WHERE transports.transport_number = :transportNumber AND transports.transport_type = :transportType")
    Maybe<List<StopEntity>> getStopsForCurrentTransport(int transportNumber, String transportType);

    @Query("SELECT * FROM transports "
            + "INNER JOIN direction ON direction.direction_transport_id = transports.transport_id "
            + "WHERE transports.transport_number = :transportNumber AND transports.transport_type = :transportType ")
    Maybe<List<DirectEntity>> getDirectionEntity(int transportNumber, String transportType);

    @Update
    void updateFavourites(TransportEntity transportEntity);

    @Query("SELECT * FROM transports WHERE favourites = '1'")
    Flowable<List<TransportEntity>> getFavouritesRoute();

    @Query("SELECT * FROM transports WHERE transport_id = :transportID ")
    Maybe<TransportEntity> getChosenTransport(long transportID);

    @Query("SELECT * FROM transports WHERE favourites = '1'")
    Maybe<List<TransportEntity>> getFavouritesRouteBeforeDeleteDB();

}
