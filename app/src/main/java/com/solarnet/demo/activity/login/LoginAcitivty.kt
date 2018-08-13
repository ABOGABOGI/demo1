package com.solarnet.demo.activity.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.solarnet.demo.MainActivity
import com.solarnet.demo.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginAcitivty : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_username.inputType = InputType.TYPE_CLASS_TEXT
        login_password.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        val login_button : Button = findViewById(R.id.login_button)

        login_button.setOnClickListener{
//            val user :String= login_username.text.toString()
//            val pass : String = login_password.text.toString()
//
//            if (user.trim().length > 0 && pass.trim().length > 0){
//                startActivity(Intent(this@LoginAcitivty, MainActivity::class.java))
//            }else{
////                Toast.makeText(applicationContext, "Please input user and password! ", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this@LoginAcitivty, MainActivity::class.java))
            //}
            startActivity(Intent(this@LoginAcitivty, MainActivity::class.java))
        }
    }
}