package com.example.al_pc.realtimegps.aplication.data.local.db.abstraction;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface IDBRepo {

    Completable addLocation(HashMap<String, Object> hashMap);

    Single<List<HashMap<String, Object>>> getLocationData();

    Single<Integer> getLocationDataSize();

    Completable removeLocationData();

}
