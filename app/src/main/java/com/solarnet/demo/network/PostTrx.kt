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
        var progressBar : ProgressBar? = null,
        var overlay : View? = null,
        var menuNext : MenuItem? = null) {
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
    var listener : TrxListener? = null

    fun postSendMoney(token : String, contact : Contact, amount : Int) : Boolean {
        val url = MyApp.URL + URL_SEND_MONEY

        val jsonContact = JSONObject().apply {
            put("name", contact.name)
            put("token", contact.token)
        }
        val json = JSONObject().apply {
            put("token", token)
            put("contact", jsonContact)
            put("amount", amount)
        }

        val body = RequestBody.create(JSON, json.toString())
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

        mClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                listener?.onFailure(call, e)
            }

            override fun onResponse(call: Call?, response: Response?) {
                val jsonString = response?.body()?.string()
                Log.i("Test", "onResponse: $jsonString")
                val json = JSONObject(jsonString)
                val trx = parseTrx(json)
                listener?.onResponse(trx)
            }
        })
        return true
    }

    private fun parseTrx(json : JSONObject) : Trx? {
        val iconId = json.getInt("iconId")
        val title = json.getString("title")
        val message = json.getString("message")
        val amount = json.getInt("amount")
        val status = json.getInt("status")
        val transactionId = json.getString("transactionId")
        val createdDateString = json.getString("createdDate")
        var createdDate : Date? = null
        try {
            createdDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(createdDateString)
            //System.out.println(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }

        val trx = Trx(iconId, title, amount, message, status, transactionId, createdDate)
        return trx
    }
}