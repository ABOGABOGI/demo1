package com.solarnet.demo.util;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String Base_URL = Constant.URLAPI;
    private static Retrofit retrofit = null;

    public static Retrofit User_Profile(){
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL + "user/profile/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit PostAuth(){
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL + "user/auth/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit Unlink(){
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL + "user/logout/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static Retrofit check_balance(){
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL + "user/balance")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit inq_remit(){
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL + "transaction/inq/remit/c2a/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit req_remit(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL+"transaction/pay/remit/c2a/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit schedule_train(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL+"product/list/kai/schedule/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit schedule_airplane(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL + "product/list/airline/schedule/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit field_airplane(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL+"product/list/airline/mandatoryfield/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }

    public static Retrofit reserve_train(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL+"reserve/ka/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit reserve_airplane(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL + "reserve/airline/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }
}
