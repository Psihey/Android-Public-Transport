package com.provectus.public_transport.persistence.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.provectus.public_transport.model.DirectionEntity;
import com.provectus.public_transport.model.IndirectionEntity;
import com.provectus.public_transport.model.PointEntity;
import com.provectus.public_transport.model.SegmentEntity;
import com.provectus.public_transport.model.StopEntity;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.persistence.dao.DirectionDao;
import com.provectus.public_transport.persistence.dao.IndirectionDao;
import com.provectus.public_transport.persistence.dao.PointDao;
import com.provectus.public_transport.persistence.dao.SegmentDao;
import com.provectus.public_transport.persistence.dao.StopDao;
import com.provectus.public_transport.persistence.dao.TransportDao;


@Database(entities = {TransportEntity.class, SegmentEntity.class, PointEntity.class, StopEntity.class, DirectionEntity.class, IndirectionEntity.class}, version = 3)
public abstract class PublicTransportDatabase extends RoomDatabase {

    public abstract TransportDao transportDao();

    public abstract SegmentDao segmentDao();

    public abstract PointDao pointDao();

    public abstract StopDao stopDao();

    public abstract IndirectionDao indirectionDao();

    public abstract DirectionDao directionDao();
}
