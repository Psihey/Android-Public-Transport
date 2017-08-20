package com.provectus.public_transport.persistent.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.provectus.public_transport.persistent.dao.PointDao;
import com.provectus.public_transport.persistent.dao.SegmentDao;
import com.provectus.public_transport.persistent.dao.TransportDao;
import com.provectus.public_transport.persistent.entity.PointEntity;
import com.provectus.public_transport.persistent.entity.SegmentEntity;
import com.provectus.public_transport.persistent.entity.TransportEntity;

/**
 * Created by Psihey on 20.08.2017.
 */

@Database(entities = {TransportEntity.class, SegmentEntity.class, PointEntity.class},version = 1)
public abstract class PublicTransportDatabase extends RoomDatabase {

    public abstract TransportDao transportDao();

    public abstract SegmentDao segmentDao();

    public abstract PointDao pointDao();
}
