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

class AccountActivity : AppCompatActivity (){

    private var account_name: String? = null
    private var account_ktp: String? = null
    private var account_number: String? = null
    private var account_alamat: String? = null
    private var account_kota: String? = null
    private var account_provinsi: String? = null
    private var account_names: EditText? = null
    private var account_ktps: EditText? = null
    private var account_numbers: EditText? = null
    private var account_alamats: EditText? = null
    private var account_kotas: EditText? = null
    private var account_provinsis: EditText? = null


    override fun  onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        var textview_profile = findViewById(R.id.account_profile) as TextView
        textview_profile.setText(Savings.getName())
        val inputAccount = findViewById(R.id.input_profile) as View
        val accountInfo = findViewById(R.id.content_profile) as View
        var btn_edit = findViewById(R.id.edit_account) as ImageButton
        var btn_save = findViewById(R.id.save_account) as Button
        btn_edit.setOnClickListener{
            inputAccount.visibility = View.VISIBLE
            accountInfo.visibility = View.GONE
        }
        btn_save.setOnClickListener {
            inputAccount.visibility = View.GONE
            accountInfo.visibility = View.VISIBLE
            savedataAccount()
        }

        account_names = findViewById(R.id.account_name)
        account_ktps = findViewById(R.id.account_ktp)
        account_numbers = findViewById(R.id.number_account)
        account_alamats = findViewById(R.id.account_alamat)
        account_kotas = findViewById(R.id.account_kota)
        account_provinsis = findViewById(R.id.account_provinsi)


        val profilename = findViewById(R.id.nameProfile) as TextView
        profilename.setText(Savings.getName())
        val profilektp = findViewById(R.id.KtpProfile) as TextView
        profilektp.setText(Savings.getAccountKtp())
        val profilenumber = findViewById(R.id.number_Profile) as TextView
        profilenumber.setText(Savings.getAccountNumber())
        val profilealamat = findViewById(R.id.AlamatProfile) as TextView
        profilealamat.setText(Savings.getAlamat())
        val profilekota = findViewById(R.id.KotaProfile) as TextView
        profilekota.setText(Savings.getKota())
        val profileprovinsi = findViewById(R.id.provinsiProfile) as TextView
        profileprovinsi.setText(Savings.getProvinsi())



    }

    private fun savedataAccount() {
        account_name = account_names.toString()
        account_ktp = account_ktps.toString()
        account_number = account_numbers.toString()
        account_alamat = account_alamats.toString()
        account_kota = account_kotas.toString()
        account_provinsi = account_provinsis.toString()
        Savings.saveName(account_name)
        Savings.saveAccountKtp(account_ktp)
        Savings.saveAccountNumber(account_number)
        Savings.saveAlamat(account_alamat)
        Savings.saveKota(account_kota)
        Savings.saveProvinsi(account_provinsi)
    }



}