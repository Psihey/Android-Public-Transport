package com.provectus.public_transport.persistence.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.provectus.public_transport.persistence.dao.PointDao;
import com.provectus.public_transport.persistence.dao.SegmentDao;
import com.provectus.public_transport.persistence.dao.TransportDao;
import com.provectus.public_transport.persistence.entity.PointEntity;
import com.provectus.public_transport.persistence.entity.SegmentEntity;
import com.provectus.public_transport.persistence.entity.TransportEntity;

/**
 * Created by Psihey on 20.08.2017.
 */

@Database(entities = {TransportEntity.class, SegmentEntity.class, PointEntity.class},version = 1)
public abstract class PublicTransportDatabase extends RoomDatabase {

    public abstract TransportDao transportDao();

    public abstract SegmentDao segmentDao();

    public abstract PointDao pointDao();
}
