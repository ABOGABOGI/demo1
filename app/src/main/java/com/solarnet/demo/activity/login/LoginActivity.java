package com.solarnet.demo.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.orhanobut.hawk.Hawk;
import com.solarnet.demo.AcitivityBase;
import com.solarnet.demo.MainActivity;
import com.solarnet.demo.R;
import com.solarnet.demo.util.APIUtils;
import com.solarnet.demo.util.ApiClient;
import com.solarnet.demo.util.ApiInterface;
import com.solarnet.demo.util.Constant;
import com.solarnet.demo.util.DoApi;
import com.solarnet.demo.util.ModelData;
import com.solarnet.demo.util.Savings;
import com.solarnet.demo.util.Util;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AcitivityBase implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    Savings savings;
    private String email;
    private String token;
    private EditText account_names;




    @Override
    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btn_submit).setOnClickListener(this);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .requestIdToken("841360253509-v0e4r5pbqsmpe6cp9e785jh2nu8phepa.apps.googleusercontent.com")
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();


    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_submit:
//                signIn();
                openHome();
                break;
        }

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Info", "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Info", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getGivenName();
            String accountName = acct.getDisplayName();
            email = acct.getEmail();
            if(acct.getPhotoUrl()!=null){
                String PhotoProfile = acct.getPhotoUrl().toString();
                savings.saveProfilPicture(PhotoProfile);
            }
            savings.saveEmail(email);
            savings.saveName(personName);
            savings.saveAccountName(accountName);
            token = result.getSignInAccount().getIdToken();
            Log.d("NP","TOKENGOOGLE:"+ token);
//            token = Util.md5(Util.md5(email)+ Constant.SALT);
//            Log.d("info",token);
            Savings.saveToken(token);

//            navigateToHome();
            sendRequest(email,token);

        }else{
            Log.d("Error","cant sign in");
        }
    }


    public class ModelLogin{
        public String email;
        public String token;
        public ModelLogin(String email,String token){
            this.email = email;
            this.token = token;
        }
    }

    private void sendRequest(String email, final String token) {
        doApi(DoApi.JSONPOSTOverrideResponse(this, "/user/auth", new ModelLogin(email, token), new DoApi.Listener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Login", response);
                ResponseLogin responseLogin = new Gson().fromJson(response,ResponseLogin.class);
                String tokens = responseLogin.token;
                Log.i("info",tokens);
                Savings.saveToken(tokens);
                openHome();
            }

            @Override
            public void onFail(String error) {
                Log.d("NP", error);
            }
        },true));
    }

    private void openHome() {
       startActivity(new Intent(this,MainActivity.class));
    }


//    {"token":"OA==",
//            "profile":{"username":"dhanangwicaksono6@gmail.com","name":null,"nik":null,"avatar":null,"address":null,"phone":null}}


    public static class ResponseLogin{
        public String token;
        public Profile profile;
        public ResponseLogin(String token,Profile profile){
            this.profile = profile;
            this.token = token;
        }
        public static class Profile{
            public String username;
            public String name;
            public int nik;
            public int avatar;
            public String address;
            public int phone;
            public Profile(String username,String name,int nik,int avatar,String address,int phone){
                this.username = username;
                this.name = name;
                this.nik = nik;
                this.avatar = avatar;
                this.address = address;
                this.phone = phone;
            }
        }
    }
}
