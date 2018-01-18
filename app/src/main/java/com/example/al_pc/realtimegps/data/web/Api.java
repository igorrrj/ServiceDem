package com.example.al_pc.realtimegps.data.web;

public class Api {

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

    public ApiQuery getQuery() {

        if (query == null) {
            query = client.getClient().create(ApiQuery.class);
        }

        return query;

    }

}
