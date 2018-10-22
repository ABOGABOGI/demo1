package com.solarnet.demo.util;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
//    public static final String Base_URL = Constant.URLAPI;
    private static Retrofit retrofit = null;

    public static Retrofit postAuth(String baseUrl){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

//    public static Retrofit User_Profile(String baseUrl){
//        if (retrofit==null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl + "user/profile/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//
//    public static Retrofit PostAuth(String baseUrl){
//        if (retrofit==null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl + "user/auth/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//
//    public static Retrofit Unlink(String baseUrl){
//        if (retrofit==null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl + "user/logout/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//    public static Retrofit check_balance(String baseUrl){
//        if (retrofit==null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl + "user/balance")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//
//    public static Retrofit inq_remit(String baseUrl){
//        if (retrofit==null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl + "transaction/inq/remit/c2a/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//
//    public static Retrofit req_remit(String baseUrl){
//        if (retrofit == null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl+"transaction/pay/remit/c2a/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//
//    public static Retrofit schedule_train(String baseUrl){
//        if (retrofit == null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl+"product/list/kai/schedule/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//
//    public static Retrofit schedule_airplane(String baseUrl){
//        if (retrofit == null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl + "product/list/airline/schedule/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//
//    public static Retrofit field_airplane(String baseUrl){
//        if (retrofit == null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl+"product/list/airline/mandatoryfield/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return  retrofit;
//    }
//
//    public static Retrofit reserve_train(String baseUrl){
//        if (retrofit == null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl+"reserve/ka/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//
//    public static Retrofit reserve_airplane(String baseUrl){
//        if (retrofit == null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl + "reserve/airline/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return  retrofit;
//    }
}
