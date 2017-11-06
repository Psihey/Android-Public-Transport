package com.provectus.public_transport.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.provectus.public_transport.model.ParkingEntity;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface ParkingDao {

    @Insert
    void insertAll(List<ParkingEntity> parkings);

    @Delete
    void deleteAll(List<ParkingEntity> parkings);

    @Query("SELECT * FROM parking ")
    Maybe<List<ParkingEntity>> getAllParkings();
}
