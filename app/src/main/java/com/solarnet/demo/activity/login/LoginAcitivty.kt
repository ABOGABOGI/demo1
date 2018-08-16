package com.solarnet.demo.activity.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.solarnet.demo.MainActivity
import com.solarnet.demo.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginAcitivty : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        var et_user_name = findViewById(R.id.et_user_name) as EditText
        var et_password = findViewById(R.id.et_password) as EditText
        var btn_submit = findViewById(R.id.btn_submit) as Button
        var txt_signup = findViewById(R.id.sign_up_edittext) as TextView

        // set on-click listener
        btn_submit.setOnClickListener {
            val user_name = et_user_name.text;
            val password = et_password.text;
//            Toast.makeText(this@LoginAcitivty, user_name, Toast.LENGTH_LONG).show()

            if (user_name.trim().length > 0 && password.trim().length > 0){
                startActivity(Intent(this@LoginAcitivty, MainActivity::class.java))
            }else{
                Toast.makeText(applicationContext, "Please check your username and password ", Toast.LENGTH_SHORT).show()
            }

            txt_signup.setOnClickListener {
                Toast.makeText(this@LoginAcitivty, "Please Sign in our Website", Toast.LENGTH_LONG).show()
            }
        }
    }
}