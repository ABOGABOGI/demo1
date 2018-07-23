package com.solarnet.demo

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class MyApp : Application() {
    private val PREF_BALANCE = "BALANCE"
    private lateinit var mPref: SharedPreferences
    companion object {
        const val DIR = "demo"
        private lateinit var sInstance: MyApp
        val instance: MyApp get() = sInstance!!

    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this

        sInstance!!.initializeInstance()
    }


    private fun initializeInstance() {

        // set application wise preference
        mPref = this.applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

    fun getBalance() : Int {
        return mPref.getInt(PREF_BALANCE, 1200000)
    }

    fun setBalance(balance : Int) {
        val edit = mPref.edit()
        edit.putInt(PREF_BALANCE, balance)
        edit.apply()
    }
}