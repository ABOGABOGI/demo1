package com.solarnet.demo

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.orhanobut.hawk.Hawk
import com.solarnet.demo.data.story.People
import com.solarnet.demo.util.Savings

class MyApp : Application() {
    private val PREF_BALANCE = "BALANCE"
    private lateinit var mPref: SharedPreferences
    companion object {
        //const val URL = "http://10.0.2.2:8000/api/"
        const val URL = "http://demo.sistemonline.biz.id/public/api/"
        const val DIR = "demo"
        private lateinit var sInstance: MyApp
        val instance: MyApp get() {
            return when (sInstance) {
                null -> MyApp()
                else -> sInstance
            }
        }

    }

    val user : String get() = "Demo User"
    val userToken : String get() = "DEMO01"

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        sInstance.initializeInstance()
        Hawk.init(this).build()
    }


    private fun initializeInstance() {
        // set application wise preference
        mPref = this.applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

    fun getBalance() : Int {
        val balance = mPref.getInt(PREF_BALANCE, 1200000)
        return balance
    }

    fun setBalance(balance : Int) {
        val edit = mPref.edit()
        edit.putInt(PREF_BALANCE, balance)
        edit.apply()
    }

    fun getMyProfile() : People {
        return People("opt01", Savings.getAccountName(),
                Savings.getProfilePicture())
//          return People("opt01", "Test",
//                Savings.getProfilePicture())
    }

}