package com.solarnet.demo.util;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

public interface ApiInterface {
    @GET("user/")
    Call<Savings> Get(@QueryMap Map<String, String> params);

//    @POST("user/")
//    @FormUrlEncoded
//    Call<ModelData> savePost(@Body ModelData data);

    @PUT("user/")
    Call<Savings> Update(@QueryMap Map<String, String> params);

    @POST("/posts")
    @FormUrlEncoded
    Call<ModelData> savePost(@Field("email") String email,
                        @Field("token") String token);
}
