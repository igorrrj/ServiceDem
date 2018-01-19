package com.example.al_pc.realtimegps.aplication.data.web;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiQuery {

    @POST("/MainServerWeb/gps/pos3")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Single<Response<ResponseBody>> postLocation(@Body List<HashMap<String, Object>> body);

}
