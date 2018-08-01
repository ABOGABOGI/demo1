package com.solarnet.demo.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.solarnet.demo.R
import com.solarnet.demo.data.trx.Trx
import com.solarnet.demo.data.trx.TrxRepository
import com.solarnet.demo.data.util.Utils

import kotlinx.android.synthetic.main.activity_trx.*

class TrxActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_TRX_ID = "trxId"
    }
    private var trxId : Long = -1L

    private lateinit var imageIcon : ImageView
    private lateinit var textTitle : TextView
    private lateinit var textAmount : TextView
    private lateinit var textMessage : TextView
    private lateinit var textTransactionId : TextView
    private lateinit var textDate : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trx)
        setSupportActionBar(toolbar)
        imageIcon = findViewById(R.id.imageIcon)
        textTitle = findViewById(R.id.textTitle)
        textAmount = findViewById(R.id.textAmount)
        textMessage = findViewById(R.id.textMessage)
        textTransactionId = findViewById(R.id.textTransactionId)
        textDate = findViewById(R.id.textDate)
        trxId = intent.getLongExtra(EXTRA_TRX_ID, 11L) //testing
        Log.i("Test", "trxId : $trxId")

        val repository : TrxRepository = TrxRepository(application)
        repository.getTrx(trxId).observe(this, Observer<List<Trx>> {trxs ->
            if (trxs != null) {
                if (trxs.size > 0) {
                    val trx = trxs[0]
                    Log.i("Test", "amount: ${trx.amount}")
                    imageIcon.setImageResource(trx.getIconResource())
                    textTitle.text = trx.title
                    textAmount.text = Utils.currencyString(trx.amount)
                    textMessage.text = trx.message
                }
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
