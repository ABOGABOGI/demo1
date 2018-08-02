package com.solarnet.demo.network

import android.util.Log
import android.widget.ProgressBar
import com.solarnet.demo.MyApp
import com.solarnet.demo.data.contact.Contact
import com.solarnet.demo.data.trx.Trx
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.RequestBody
import android.view.View
import android.view.MenuItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PostTrx(
        private var mToken : String = MyApp.instance.userToken) {
    val JSON = MediaType.parse("application/json; charset=utf-8")
    var isRunning = false

    interface TrxListener {
        fun onResponse(trx : Trx?)
        fun onErrorResponse(msg : String)
        fun onFailure(call : Call?, exception : Exception?)
    }

    private var mClient : OkHttpClient = OkHttpClient.Builder()
            .cache(null)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()

    val URL_SEND_MONEY = "sendmoney"
    val URL_CELLULAR = "cellular"
    val URL_PLN = "pln"
    val URL_WITHDRAW = "withdraw"

    var listener : TrxListener? = null
    val callback : Callback = object : Callback {
        override fun onFailure(call: Call?, e: IOException?) {
            listener?.onFailure(call, e)
        }

        override fun onResponse(call: Call?, response: Response?) {
            try {
                val jsonString = response?.body()?.string()
                Log.i("Test", "onResponse: $jsonString")
                val json = JSONObject(jsonString)
                val trx = parseTrx(json)
                listener?.onResponse(trx)
            } catch (e : Exception) {
                listener?.onErrorResponse(e.message.toString())
            }
        }

    }

    fun postPln(meterNo : String, amount : Int)
            : Boolean {
        val url = MyApp.URL + URL_PLN
        val json = JSONObject().apply {
            put("token", mToken)
            put("meterNo", meterNo)
            put("amount", amount)
        }

        sendRequest(url, json)
        return true
    }

    fun postCellular(phone : String, type : String, product : String, productCode : String, amount : Int)
            : Boolean {
        val url = MyApp.URL + URL_CELLULAR
        val json = JSONObject().apply {
            put("token", mToken)
            put("phone", phone)
            put("type", type)
            put("product", product)
            put("productCode", productCode)
            put("amount", amount)
        }

        sendRequest(url, json)

        return true
    }

    fun postWithdraw(bankName : String, bankCode : String, bankAccount : String, amount : Int) : Boolean {
        val url = MyApp.URL + URL_WITHDRAW

        val jsonBank = JSONObject().apply {
            put("name", bankName)
            put("code", bankCode)
            put("account", bankAccount)
        }
        val json = JSONObject().apply {
            put("token", mToken)
            put("bank", jsonBank)
            put("amount", amount)
        }
        sendRequest(url, json)

        return true
    }

    fun postSendMoney(contact : Contact, amount : Int) : Boolean {
        val url = MyApp.URL + URL_SEND_MONEY

        val jsonContact = JSONObject().apply {
            put("name", contact.name)
            put("token", contact.token)
        }
        val json = JSONObject().apply {
            put("token", mToken)
            put("contact", jsonContact)
            put("amount", amount)
        }
        sendRequest(url, json)

        return true
    }

    private fun sendRequest(url : String, json : JSONObject) {

        val body = RequestBody.create(JSON, json.toString())
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

        mClient.newCall(request).enqueue(callback)
    }
    private fun parseTrx(json : JSONObject) : Trx? {
        try {
            val iconId = json.getInt("iconId")
            val title = json.getString("title")
            val message = json.getString("message")
            val amount = json.getInt("amount")
            val status = json.getInt("status")
            val transactionId = json.getString("transactionId")
            val createdDateString = json.getString("createdDate")
            val createdDate: Date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(createdDateString)

            return Trx(iconId, title, amount, message, status, transactionId, createdDate)
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null
    }
}