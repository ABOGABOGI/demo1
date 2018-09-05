package com.solarnet.demo.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.orhanobut.hawk.Hawk;
import com.solarnet.demo.MainActivity;
import com.solarnet.demo.R;
import com.solarnet.demo.util.ApiClient;
import com.solarnet.demo.util.ApiInterface;
import com.solarnet.demo.util.Constant;
import com.solarnet.demo.util.Savings;
import com.solarnet.demo.util.Util;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    Savings savings;
    private String email;
    private String token;



    @Override
    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_submit:
                signIn();
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

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Info", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            email = acct.getEmail();
            String PhotoProfile = acct.getPhotoUrl().toString();
            savings.saveEmail(email);
            savings.saveName(personName);
            savings.saveProfilPicture(PhotoProfile);
//            if (acct.getPhotoUrl() != null){
//                Savings.saveProfilPicture(profilpic);
//            }else{
//                Log.d("Info","No Photo");
//            }


            token = Util.md5(Util.md5(email)+ Constant.SALT);
            Savings.saveToken(token);

            navigateToHome();

        }else{
            Log.d("Error","cant sign in");
        }
    }

    private void navigateToHome() {
        ApiInterface apiInterface = ApiClient.PostAuth().create(ApiInterface.class);
        Map<String,String> queryparams = new HashMap<>();
        queryparams.put("email",email);
        queryparams.put("token",token);

        Call<Savings> call = apiInterface.Post(queryparams);
        call.enqueue(new Callback<Savings>() {
            @Override
            public void onResponse(Call<Savings> call, Response<Savings> response) {
                Log.d("info",token);
                openHome();
            }

            @Override
            public void onFailure(Call<Savings> call, Throwable t) {
                Toast.makeText(LoginActivity.this,
                        "Error is " + t.getMessage()
                        , Toast.LENGTH_LONG).show();
            }
        });

    }

    private void openHome() {
       startActivity(new Intent(this,MainActivity.class));
    }
}
