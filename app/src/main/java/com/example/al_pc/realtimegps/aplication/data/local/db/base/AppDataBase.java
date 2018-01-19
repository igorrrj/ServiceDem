package com.example.al_pc.realtimegps.aplication.data.local.db.base;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.al_pc.realtimegps.aplication.data.local.db.dao.GpsDao;
import com.example.al_pc.realtimegps.aplication.data.local.db.entity.GpsEntity;


@Database(entities = {GpsEntity.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public static final String NAME = "gps";

    public abstract GpsDao getGpsDao();

}
