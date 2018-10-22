package com.solarnet.demo.util;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {


    @POST("/posts")
    @FormUrlEncoded
    Call<ModelData> savePost(
                        @Field("email") String email,
                        @Field("token") String token);

    @PUT("/posts/{id}")
    @FormUrlEncoded
    Call<ModelData> updatePost(@Path("id") long id,
                          @Field("title") String title,
                          @Field("body") String body,
                          @Field("userId") long userId);

    @DELETE("/posts/{id}")
    Call<ModelData> deletePost(@Path("id") long id);

    //This method is used for "POST"
    @FormUrlEncoded
    @POST("/api.php")
    Call<ModelData> post(
            @Field("method") String method,
            @Field("username") String username,
            @Field("password") String password
    );

    //This method is used for "GET"
    @GET("/api.php")
    Call<ModelData> get(
            @Query("method") String method,
            @Query("username") String username,
            @Query("password") String password
    );

}
