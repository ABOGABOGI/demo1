package com.solarnet.demo.util;

public class APIUtils {

    private APIUtils (){}

    public static final String BASE_URL = Constant.URLAPI;

    public static ApiInterface getAPIService() {

        return ApiClient.postAuth(BASE_URL).create(ApiInterface.class);
    }
}
