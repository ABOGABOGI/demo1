package com.solarnet.demo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.orhanobut.hawk.Hawk
import com.solarnet.demo.activity.login.IntroActivity
import com.solarnet.demo.activity.login.PrefManager
import com.solarnet.demo.util.Savings
import android.content.SharedPreferences



class SplashScreen : AppCompatActivity(){

    val handler = Handler()
    val runnable = Runnable {
        if(Savings.getAccountName() != null){
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        }else{
            startActivity(Intent(this@SplashScreen, IntroActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,1000)
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
