package com.solarnet.demo.util;

public class APIUtils {

    private APIUtils (){}

    public static final String BASE_URL = Constant.URLAPI;

    public static ApiInterface getAPIService() {

        return ApiClient.PostAuth(BASE_URL).create(ApiInterface.class);
    }
}
