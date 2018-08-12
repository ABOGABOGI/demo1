package com.solarnet.demo.activity

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.solarnet.demo.R
import com.solarnet.demo.activity.payment.TopUpActivity
import com.solarnet.demo.data.trx.Trx
import com.solarnet.demo.data.trx.TrxRepository
import com.solarnet.demo.data.util.Utils

import kotlinx.android.synthetic.main.activity_trx.*
import org.json.JSONObject
import java.util.ArrayList
import android.content.Intent
import android.net.Uri


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
        trxId = intent.getLongExtra(EXTRA_TRX_ID, 14L) //testing
        Log.i("Test", "trxId : $trxId")

        val repository : TrxRepository = TrxRepository(application)
        repository.getTrx(trxId).observe(this, Observer<List<Trx>> {trxs ->
            if (trxs != null) {
                if (trxs.isNotEmpty()) {
                    val trx = trxs[0]
                    Log.i("Test", "amount: ${trx.amount}")
                    imageIcon.setImageResource(trx.getIconResource())
                    textTitle.text = trx.title
                    textAmount.text = Utils.currencyString(trx.amount)
                    textMessage.text = trx.message
                    textTransactionId.text = trx.transactionId
                    textDate.text = Utils.generateDateString(trx.createdDate)
                    if (!trx.data.isNullOrBlank()) {
                        setFragment(trx)
                    }
                }
            }
        })
    }

    private fun setFragment(trx : Trx) {
        var fragment : Fragment? =
        when (trx.getIconResource()) {
            R.drawable.ic_topup -> {
                val jsonData = JSONObject(trx.data)
                TopUpActivity.ManualFragment.newInstance(
                        TopUpActivity.getBankIcon(jsonData.getString("bankName")),
                        jsonData.getString("bankName"),
                        jsonData.getString("bankAccount"),
                        trx.amount,
                        jsonData.getInt("unique"),
                        Utils.formatDate(trx.expiredDate!!))
            }
            R.drawable.ic_invoice -> {
                val jsonData = JSONObject(trx.data)
                InvoiceFragment.newInstance(jsonData.getString("url"))
            }
            else -> null
        }
        if (fragment != null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fragment, fragment)
                commit()
            }
        }
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


    class InvoiceFragment : Fragment() {
        companion object {
            val URL = "url"
            fun newInstance(url : String) : InvoiceFragment {
                val f = InvoiceFragment()
                val args = Bundle().apply {
                    putString(URL, url)
                }
                f.arguments = args
                return f
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_trxinvoice, container, false)
            val buttonLink : Button = view.findViewById(R.id.buttonLink)
            buttonLink.text = arguments?.getString(URL)
            buttonLink.setOnClickListener { v ->
                val url = (v as Button).text.toString()
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
            return view
        }

    }
}
