package com.example.al_pc.realtimegps.aplication.data.local.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.al_pc.realtimegps.aplication.data.local.db.DBRepo;
import com.example.al_pc.realtimegps.aplication.data.local.db.base.AppDataBase;

@Entity(tableName = AppDataBase.NAME)
public class GpsEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = DBRepo.TRANSPORT_ID)
    private String transportId;

    @ColumnInfo(name = DBRepo.SPEED)
    private float speed;

    @ColumnInfo(name = DBRepo.LATITUDE)
    private double latitude;

    @ColumnInfo(name = DBRepo.LONGITUDE)
    private double longitude;

    @ColumnInfo(name = DBRepo.DATE)
    private long date;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getTransportId() {
        return transportId;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

}
