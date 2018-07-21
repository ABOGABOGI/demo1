package com.solarnet.demo.activity.payment

import android.app.Activity
import android.arch.lifecycle.*
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.solarnet.demo.R
import com.solarnet.demo.adapter.NumPadAdapter
import com.solarnet.demo.data.util.Utils

import kotlinx.android.synthetic.main.activity_send_money.*
import kotlinx.android.synthetic.main.content_send_money.*
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.Log
import com.solarnet.demo.data.AppRoomDatabase
import com.solarnet.demo.data.contact.Contact
import com.solarnet.demo.data.contact.ContactRepository
import com.solarnet.demo.data.trx.TrxRepository

import android.app.Application
import android.content.Intent
import android.view.View
import com.google.gson.Gson
import com.solarnet.demo.activity.ContactActivity

class SendMoneyActivity : AppCompatActivity(), NumPadAdapter.OnNumPadListener {

    private lateinit var mViewModel : AppViewModel
    private var menu : Menu? = null
    private val INITIAL_BALANCE = 2500000 //test only
    private val ACTIVITY_CONTACT = 1212

    override fun onNumPadKey(key: String) {
        if (mViewModel!!.amount!!.compareTo("0") != 0) {
            mViewModel!!.amount += key
        } else {
            mViewModel!!.amount = key
        }

        updateViews()
    }

    private fun updateViews() {
        var amount = mViewModel.amount.toIntOrNull()
        if (amount != null) {
            editAmount.setText(Utils.currencyString(amount))
            mViewModel.newBalance = mViewModel.balance - amount!!
            textBallance.text = Utils.currencyString(mViewModel.newBalance)
        } else {
            mViewModel.amount = "0"
            editAmount.setText("0")
            mViewModel.newBalance = mViewModel.balance
            textBallance.text = Utils.currencyString(mViewModel.newBalance)
        }

        if (mViewModel.newBalance >= 0) {
            textBallance.setTextColor(ContextCompat.getColor(this,
                    android.R.color.primary_text_light))
        } else {
            textBallance.setTextColor(Color.RED)
        }


        if (amount == null) amount = 0
        updateMenuStatus(amount)

    }

    private fun updateMenuStatus(amount : Int) {
        menu?.findItem(R.id.action_next)?.isEnabled = mViewModel.newBalance >= 0 && amount > 0 &&
                mViewModel.contact != null
    }

    override fun onNumPadBackspace() {
        if (mViewModel.amount.compareTo("0") != 0)  {
            if (mViewModel.amount.length <= 1) {
                mViewModel.amount = "0"
            } else {
                mViewModel.amount = mViewModel!!.amount.substring(0,
                    mViewModel.amount.length - 1)
            }
            updateViews()

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_money)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        mViewModel.balance = INITIAL_BALANCE
        updateContactViews()
        updateViews()
        editContact.setOnClickListener{ _ ->
            if (mViewModel.contact == null) {
                startActivityForResult(Intent(this, ContactActivity::class.java).apply {
                    putExtra(ContactActivity.EXTRA_STRING_TITLE, getString(R.string.recipient))
                }, ACTIVITY_CONTACT)
            }
        }
        buttonCancel.setOnClickListener { _ ->
            mViewModel.contact = null
            updateContactViews()
        }

        editAmount.setText(mViewModel!!.amount)
        textBallance.text = Utils.currencyString(mViewModel.newBalance)

        recyclerNumPad.layoutManager = GridLayoutManager(this, 3)!!
        recyclerNumPad.adapter = NumPadAdapter().apply { listener = this@SendMoneyActivity }
        recyclerNumPad.setHasFixedSize(true)
    }

    private fun updateContactViews() {
        if (mViewModel?.contact == null) {
            editContact.visibility = View.VISIBLE
            editSelectedContact.visibility = View.INVISIBLE
            imageIcon.visibility = View.INVISIBLE
            buttonCancel.visibility = View.INVISIBLE
        } else {
            editContact.visibility = View.GONE
            editSelectedContact.visibility = View.VISIBLE
            imageIcon.visibility = View.VISIBLE
            buttonCancel.visibility = View.VISIBLE
            editSelectedContact.setText(mViewModel!!.contact!!.name)
        }

        var amount = mViewModel.amount.toIntOrNull()
        if (amount == null) amount = 0
        updateMenuStatus(amount)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.send_payment, menu)
        menu.findItem(R.id.action_next).isEnabled = false
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_next -> {
                //proceed send money
                true
            }
            android.R.id.home -> {
                super.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ACTIVITY_CONTACT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (data.hasExtra(ContactActivity.EXTRA_CONTACT)) {
                        try {
                            val contact = Gson().fromJson(
                                    data.getStringExtra(ContactActivity.EXTRA_CONTACT),
                                    Contact::class.java)
                            mViewModel.contact = contact
                        } catch (e : Exception) {
                            mViewModel.contact = null
                        }
                        updateContactViews()
                    }
                }
            }
        }
    }

    class AppViewModel(application : Application) : AndroidViewModel(application) {
        var amount: String = "0"
        var balance: Int = 0
        var newBalance: Int = 0
        var contact: Contact? = null
    }
}
