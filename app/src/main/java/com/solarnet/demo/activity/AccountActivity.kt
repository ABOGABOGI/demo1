package com.solarnet.demo.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.solarnet.demo.R
import com.solarnet.demo.util.Savings
import kotlinx.android.synthetic.main.activity_profil.view.*

class AccountActivity : AppCompatActivity (){

    private var account_names: String? = null


    override fun  onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        var textview_profile = findViewById(R.id.account_profile) as TextView
        textview_profile.setText(Savings.getName())
        val inputAccount = findViewById(R.id.input_profile) as View
        val accountInfo = findViewById(R.id.content_profile) as View
        var btn_edit = findViewById(R.id.edit_account) as ImageButton
        var btn_save = findViewById(R.id.saveProfile) as Button
                btn_edit.setOnClickListener{
            inputAccount.visibility = View.VISIBLE
            accountInfo.visibility = View.GONE
        }
        btn_save.setOnClickListener {
            inputAccount.visibility = View.GONE
            accountInfo.visibility = View.VISIBLE
        }

        var account_name = findViewById(R.id.account_name) as EditText

    }

}