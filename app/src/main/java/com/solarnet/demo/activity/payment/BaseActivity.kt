package com.solarnet.demo.activity.payment

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.solarnet.demo.R
import com.solarnet.demo.activity.TrxActivity
import com.solarnet.demo.data.trx.Trx
import com.solarnet.demo.data.trx.TrxRepository
import com.solarnet.demo.network.PostTrx
import kotlinx.android.synthetic.main.activity_send_money.*
import okhttp3.Call

abstract class BaseActivity : AppCompatActivity(), PostTrx.TrxListener {
    var menuNext : MenuItem? = null
    var mPostTrx = PostTrx().apply { listener = this@BaseActivity }
    abstract fun next()
    open fun getProgressBar() : ProgressBar? {
        try {
            return findViewById(R.id.progressBar)
        } catch (e : Exception) {}
        return null
    }

    open fun getOverlay() : View? {

        try {
            val view : View = findViewById(R.id.overlay)
            return view
        } catch (e : Exception) {}
        return null
    }

    open fun back() {
        super.onBackPressed()
    }
    open fun getTrxRepository() : TrxRepository? { return null }


    fun showToast(message : String) {
        runOnUiThread{
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun showProgress(show : Boolean) {
        runOnUiThread {
            if (show) {
                getProgressBar()?.visibility = View.VISIBLE
                getOverlay()?.visibility = View.VISIBLE
                menuNext?.isEnabled = false
            } else {
                getProgressBar()?.visibility = View.GONE
                getOverlay()?.visibility = View.GONE
                menuNext?.isEnabled = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.send_payment, menu)
        menuNext = menu.findItem(R.id.action_next)
        menuNext?.isEnabled = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_next -> {
                next()
                true
            }
            android.R.id.home -> {
                back()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onResponse(trx: Trx?) {
        showProgress(false)
        if (trx != null) {
            getTrxRepository()!!.insert(trx, object : TrxRepository.OnInsertListener {
                override fun onInsert(id: Long) {
                    startActivity(Intent(this@BaseActivity,
                            TrxActivity::class.java).apply {
                        putExtra(TrxActivity.EXTRA_TRX_ID, id)
                    })
                    finish()
                }
            })
        } else {
            showToast("Error server response!")
        }
    }

    override fun onErrorResponse(msg: String) {
        showProgress(false)
        showToast("Failed : $msg")
    }

    override fun onFailure(call: Call?, exception: Exception?) {
        showProgress(false)
        showToast("Failed : ${exception?.message?.toString()}")
    }
}