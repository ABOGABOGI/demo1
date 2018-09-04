package com.solarnet.demo.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String Base_URL = Constant.URLAPI;
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit(){
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit PostAuth(){
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL + "user/profile/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
