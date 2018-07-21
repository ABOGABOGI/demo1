package com.solarnet.demo.activity.payment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.solarnet.demo.R

abstract class BaseActivity : AppCompatActivity() {
    var menuNext : MenuItem? = null

    abstract fun next()

    open fun back() {
        super.onBackPressed()
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
}