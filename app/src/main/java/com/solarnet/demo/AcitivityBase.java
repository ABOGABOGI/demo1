package com.solarnet.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.solarnet.demo.util.DoApi;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public abstract class AcitivityBase extends AppCompatActivity {


    RequestQueue requestQueue;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestQueue = Volley.newRequestQueue(this,new SslPinning());
        requestQueue = Volley.newRequestQueue(this);
//        setContentView(R.layout.activity_back_toolbar);
//        LayoutInflater.from(this).inflate(getLayout(), (FrameLayout) findViewById(R.id.container), true);
//        insideToolbar = findViewById(R.id.toolbar_inside);
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("");
//        TextView textView = inflateToolbar(R.layout.toolbar_plaintext).findViewById(R.id.txt_title);
//        textView.setTextColor(ContextCompat.getColor(this, getPageTitleColor()));
//        textView.setText(getPageTitle());
    }

    boolean onCreate = true;

    @Override
    protected void onStart() {
        super.onStart();
        if (!onCreate) requestQueue = Volley.newRequestQueue(this);
        else onCreate = false;
    }

    protected void doApi(StringRequest stringRequest) {
        stringRequest.setTag(this.getClass().getSimpleName());
        requestQueue.add(stringRequest);
    }



    @Override
    protected void onStop() {
        super.onStop();
        requestQueue.cancelAll(this.getClass().getSimpleName());
    }

}
