package com.example.al_pc.realtimegps.aplication.data.local.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.al_pc.realtimegps.aplication.data.local.db.abstraction.IDBRepo;
import com.example.al_pc.realtimegps.aplication.data.local.db.base.AppDataBase;
import com.example.al_pc.realtimegps.aplication.data.local.db.entity.GpsEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class DBRepo implements IDBRepo {

    public static final String TRANSPORT_ID = "transportId";
    public static final String SPEED = "speed";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String DATE = "date";

    private static AppDataBase appDataBase;

    private static DBRepo dbRepo;

    private DBRepo() {
    }

    public static DBRepo getInstance(Context context) {

        if (appDataBase == null) {
            appDataBase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class, AppDataBase.NAME)
                    .build();
        }

        if (dbRepo == null) {
            dbRepo = new DBRepo();
        }
        return dbRepo;
    }

    @Override
    public Completable addLocation(HashMap<String, Object> hashMap) {
        return Completable.fromAction(() -> appDataBase.getGpsDao().insetItem(transform(hashMap)))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<HashMap<String, Object>>> getLocationData() {

        return Single.fromCallable(() -> appDataBase.getGpsDao().getAllItems())
                .map(
                        gpsEntities ->{

                            List<HashMap<String, Object>> list = new ArrayList<>();
                            for (GpsEntity gpsEntity : gpsEntities){

                                list.add(transform(gpsEntity));

                            }
                            return list;
                        }
                ).subscribeOn(Schedulers.io());

    }

    @Override
    public Single<Integer> getLocationDataSize() {
        return Single.just(appDataBase.getGpsDao().getAllItems().size())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable removeLocationData() {
        return Completable.fromAction(() -> appDataBase.getGpsDao().deleteAllItems())
                .subscribeOn(Schedulers.io());
    }

    private GpsEntity transform(HashMap<String, Object> hashMap) {

        GpsEntity gpsEntity = new GpsEntity();
        gpsEntity.setTransportId((String) hashMap.get(DBRepo.TRANSPORT_ID));
        gpsEntity.setDate((Long) hashMap.get(DBRepo.DATE));
        gpsEntity.setSpeed((Float) hashMap.get(DBRepo.SPEED));
        gpsEntity.setLatitude((Double) hashMap.get(DBRepo.LATITUDE));
        gpsEntity.setLongitude((Double) hashMap.get(DBRepo.LONGITUDE));

        return gpsEntity;
    }

    private HashMap<String, Object> transform(GpsEntity gpsEntity) {

        HashMap<String, Object> map = new HashMap<>();
        map.put(DBRepo.TRANSPORT_ID, gpsEntity.getTransportId());
        map.put(DBRepo.SPEED, gpsEntity.getSpeed());
        map.put(DBRepo.LATITUDE, gpsEntity.getLatitude());
        map.put(DBRepo.LONGITUDE, gpsEntity.getLongitude());
        map.put(DBRepo.DATE, gpsEntity.getDate());

        return map;
    }

}
