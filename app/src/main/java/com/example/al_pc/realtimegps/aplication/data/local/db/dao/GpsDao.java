package com.example.al_pc.realtimegps.aplication.data.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.al_pc.realtimegps.aplication.data.local.db.entity.GpsEntity;

import java.util.List;

@Dao
public interface GpsDao {

    @Insert()
    void insetItem(GpsEntity... products);

    @Query("SELECT * FROM gps")
    List<GpsEntity> getAllItems();

    @Query("DELETE FROM gps")
    void deleteAllItems();

    @Query("SELECT COUNT(*) from gps")
    int countData();

}
