package com.solarnet.demo.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.solarnet.demo.R
import com.solarnet.demo.util.Savings
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.icon_list_item.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import android.content.SharedPreferences
import com.solarnet.demo.MainActivity


class ProfileActivity : AppCompatActivity() {


    private val SHARED_PREF_NAME = "mysharedpref"
    private val KEY_NAME = "keyname"


    override fun  onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)


        val listviewProfile = findViewById(R.id.listview_profile) as ListView
//        val v = findViewById(R.id.input_profile) as View
//        val v1 = findViewById(R.id.content_profile) as View
        var textview_profile = findViewById(R.id.textview_profile) as TextView
        textview_profile.setText(Savings.getName())
//        var btn_save = findViewById(R.id.saveProfile) as Button
//        var btn_edit = findViewById(R.id.edit_profile) as ImageButton
//        btn_edit.setOnClickListener{
//            v.visibility = View.VISIBLE
//            v1.visibility = View.GONE
//            linearProfile.visibility = View.GONE
//        }
//
//        btn_save.setOnClickListener {
//            v.visibility = View.GONE
//            linearProfile.visibility = View.VISIBLE
//
//        }

        val values = arrayOf(
                "Account",
                "About",
                "Settings",
                "LogOut")

        val adapter = ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,values)
        listviewProfile.adapter = adapter

        listviewProfile.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when(position){
                0 -> startActivity(Intent(this,AccountActivity::class.java))
                1 -> startActivity(Intent(this,MainActivity::class.java))
                2 -> startActivity(Intent(this,MainActivity::class.java))
                3 -> startActivity(Intent(this,MainActivity::class.java))
            }

        }


    }



}

