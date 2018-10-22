package com.solarnet.demo.util;

import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.solarnet.demo.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class DoApi {

    RequestQueue requestQueue;

    public interface Listener{
        void onSuccess(String response);
        void onFail(String error);
    }

    public static class Req {
        public Object req;
        //        public String trxid;
//        public String token;
        public Req(Object req){
            HashMap<String,String> hashMap = new Gson().fromJson(new Gson().toJson(req),new TypeToken<HashMap<String,String>>(){}.getType());
            hashMap.put("token",Savings.getToken());
//            hashMap.put("trxid",Util.trxiIDGenerator());
            this.req = hashMap;
//            this.trxid = Util.trxiIDGenerator();
//            this.token = Savings.getToken();
//            this.token = "29cd7a264034a0d58f982274124f8596";
        }
    }

    public static StringRequest GET(final Activity activity, String url, HashMap<String,String> keyValue, final Listener listener){
        if(keyValue ==null) keyValue = new HashMap<>();
//        keyValue.put("mid",""+Savings.getMID(Constant.FEATURE_MINIATM));
        StringBuilder sb = new StringBuilder();
        for(HashMap.Entry<String, String> e : keyValue.entrySet()){
            if(sb.length() > 0){
                sb.append('&');
            }
            try {
                sb.append(URLEncoder.encode(e.getKey(), "UTF-8")).append('=').append(URLEncoder.encode(e.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e1) {

            }
        }
        final String finalURL = Constant.URLAPI+url+"?"+sb.toString();
        Log.d("XB","FNIALURL GET: "+finalURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("XB","SUCCESS:"+response);
                        listener.onSuccess(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null&&error.networkResponse!=null&&error.networkResponse.statusCode==422){
                    //Error
                    try {
                        String body = new String(error.networkResponse.data,"UTF-8");
                        final ModelResponse responseModel = new Gson().fromJson(body,ModelResponse.class);
                        Log.i("Info", String.valueOf(responseModel));
//                        Generic.showAPIErrorDialog(activity, responseModel.error.code, responseModel.error.description, new Generic.ListenerAPIError() {
//                            @Override
//                            public void onDismissed() {
//                                listener.onFail(responseModel.error.description);
//                            }
//                        });
                        return;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                try{
                    if(error!=null) Log.d("XB","NPREQ:STEP:E:"+error.getLocalizedMessage());
                    listener.onFail("Error");
                }catch (Exception e){
                    Log.d("XB","NPREQ:STEP:E:"+e.getLocalizedMessage());
                    listener.onFail(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> header = new HashMap<>();
//                header.put("sn",ApplicationMainMandiri.getInstance().getSerialNumber());
                header.put("pbuild",""+ BuildConfig.VERSION_CODE);
                return header;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return stringRequest;
    }

    public static StringRequest JSONPOSTOverrideResponse(final Activity activity, String url, final Object body, final Listener listener, final boolean isOverride){
        final String finalURL = Constant.URLAPI+url;
        final String LOGIN_SUCCESS = "status";

        Log.d("XB","FNIALURL JSONPOSTOverrideResponse:"+finalURL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, finalURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("XB","SUCCESS:"+response);
                        int i = response.length()-100;
                        if(i<0) i = 0;
                        Log.d("XB","SUCCESS:END:"+response.substring(i,response.length()));
                        if(isOverride){
                            listener.onSuccess(response.toString());
                            return;
                        }
                        try{
                            final ModelResponse responseModel = new Gson().fromJson(response,ModelResponse.class);
                            if(responseModel.status==1){
//                                Generic.showAPIErrorDialog(activity, responseModel.error.code, responseModel.error.descriptions, new Generic.ListenerAPIError() {
//                                    @Override
//                                    public void onDismissed() {
//                                        listener.onFail(TextUtils.join(" ",responseModel.error.descriptions));
//                                        Util.showMainMenu(activity);
//                                    }
//                                });
                            }else {
//                                if(responseModel.token!=null && !responseModel.token.equals("")){
//                                    Savings.saveToken(responseModel.token);
//                                }
                                listener.onSuccess(response.toString());
                            }

                        }catch (Exception e){
                            listener.onSuccess(response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null&&error.networkResponse!=null&&error.networkResponse.statusCode==422){
                    //Error
                    try {
                        String body = new String(error.networkResponse.data,"UTF-8");
                        final ModelResponse responseModel = new Gson().fromJson(body,ModelResponse.class);
//                        Generic.showAPIErrorDialog(activity, responseModel.error.code, responseModel.error.description, new Generic.ListenerAPIError() {
//                            @Override
//                            public void onDismissed() {
//                                listener.onFail(responseModel.error.description);
//                            }
//                        });
                        return;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                try{
                    if(error!=null) Log.d("XB","NPREQ:STEP:E:"+error.getLocalizedMessage());
                    listener.onFail("Error");
                }catch (Exception e){
                    Log.d("XB","NPREQ:STEP:E:"+e.getLocalizedMessage());
                    listener.onFail(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    String jsonBody = new Gson().toJson(body).replace("\\u003d","=");
                    Log.d("XB","JSONBODY:"+jsonBody);
                    HashMap<String,String> bodymap = new HashMap<>();
//                    SharedPreferences settings = Context.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE);
//                    String _token = settings.getString(PREFS_TOKEN, "");
//                    String token = Savings.getToken().token;
//                    String trxid = Util.trxiIDGenerator();
                    return jsonBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return stringRequest;
    }

    public static StringRequest JSONPOST(final Activity activity, String url, final Object body, final Listener listener){
        return JSONPOSTOverrideResponse(activity,url,body,listener,false);
    }


}
