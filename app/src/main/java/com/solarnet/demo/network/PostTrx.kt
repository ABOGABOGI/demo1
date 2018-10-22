package com.solarnet.demo.network

import android.util.Log
import com.solarnet.demo.MyApp
import com.solarnet.demo.data.contact.Contact
import com.solarnet.demo.data.trx.Trx
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.RequestBody
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

    private val URL_SEND_MONEY = "sendmoney"
    private val URL_CELLULAR = "cellular"
    private val URL_PLN = "pln"
    private val URL_WITHDRAW = "withdraw"
    private val URL_TOPUP_1 = "topup1"
    private val URL_PRICE = "price"
    private val URL_PAYMENTQR = "payment"
    private val URL_INVOICE = "invoice"
    private val URL_AUTH = "user/auth/"

    var listener : TrxListener? = null
    private val callback : Callback = object : Callback {
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

//    fun postAuth(email: String?, token: String?, callback: javax.security.auth.callback.Callback) : Boolean{
//        val url = MyApp.URL + URL_AUTH
//        val json = JSONObject().apply {
//            put("email",email)
//            put("token",token)
//        }
//        sendRequest(url, json,callback)
//        return true
//
//    }

    fun postPaymentQr(code : String, product : String, amount : Int) : Boolean {
        val url = MyApp.URL + URL_PAYMENTQR
        try {
            val json = JSONObject().apply {
                put("token", mToken)
                put("amount", amount)
                put("code", code)
                put("product", product)
            }

            sendRequest(url, json)
        } catch (e : Exception) {
            listener?.onErrorResponse(e.message.toString())
        }
        return true
    }

    fun postManualTopUp1(amount : Int, bankCode : String, bankName : String) : Boolean {
        val url = MyApp.URL + URL_TOPUP_1
        val jsonBank = JSONObject().apply {
            put("name", bankName)
            put("code", bankCode)
        }
        val json = JSONObject().apply {
            put("token", mToken)
            put("amount", amount)
            put("bank", jsonBank)
        }

        sendRequest(url, json)
        return true
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

    fun getPrice(code : String, callback : Callback) {
        var url = MyApp.URL + URL_PRICE
        val json = JSONObject().apply {
            put("code", code)
        }
        sendRequest(url, json, callback)
    }

    fun postInvoice(amount : Int, bankName : String, bankAccount : String, message : String)
            : Boolean {
        val url = MyApp.URL + URL_INVOICE
        val json = JSONObject().apply {
            put("token", mToken)
            put("amount", amount)
            put("bankName", bankName)
            put("bankAccount", bankAccount)
            put("message", message)
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

    private fun sendRequest(url : String, json : JSONObject, cback : Callback? = null) {
        val cb : Callback = when (cback) {
            null -> callback
            else -> cback
        }

        val body = RequestBody.create(JSON, json.toString())
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

        mClient.newCall(request).enqueue(cb)
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
            val trx = Trx(iconId, title, amount, message, status, transactionId, createdDate)
            if (json.has("expiredDate")) {
                val expiredDateString = json.getString("expiredDate")
                if (expiredDateString != null || expiredDateString?.compareTo("null", true) != 0) {
                    try {
                        trx.expiredDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(expiredDateString)
                    } catch (pe : ParseException) {
                        ;
                    }
                }
            }
            if (json.has("data")) {
                val data = json.getString("data")
                if (data.compareTo("null", true) != 0 || !data.isNullOrBlank()) {
                    try {
                        JSONObject(data) //try to parse
                        trx.data = data
                    } catch (pe : Exception) {
                        ;
                    }
                }
            }
            return trx
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null
    }
}