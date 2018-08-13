package com.solarnet.demo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.solarnet.demo.activity.login.LoginAcitivty

class SplashScreen : AppCompatActivity(){

    val handler = Handler()
    val runnable = Runnable {
        startActivity(Intent(this@SplashScreen, LoginAcitivty::class.java))
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,2000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }



}