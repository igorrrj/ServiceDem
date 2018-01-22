package com.example.al_pc.realtimegps.aplication.data.web;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class Api {

    private static final String BASE_URL = "https://pcounterdev.gemicle.com";
    private static Api api;
    private static ApiClient client;
    private ApiQuery query;

    public static Api getInstance() {

        if (api == null) {
            api = new Api();
        }

        if (client == null)
            client = new ApiClient();

        return api;

    }

    private ApiQuery getQuery() {

        if (query == null) {
            query = client.getClient(BASE_URL).create(ApiQuery.class);
        }

        return query;

    }


    public Completable sendLocation(List<HashMap<String, Object>> data){

        return getQuery().postLocation(data)
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(this::handleResponse);

    }


    private Completable handleResponse(retrofit2.Response response) {

        return Completable.create(

                e -> {

                    if (response.code() == 200)
                        e.onComplete();
                    else
                        e.onError(new Throwable(response.message()));

                }

        );

    }


}
