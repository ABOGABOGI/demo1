package com.solarnet.demo.activity.payment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.solarnet.demo.R
import com.solarnet.demo.adapter.PaymentGridViewAdapter
import com.solarnet.demo.data.GridItem

import kotlinx.android.synthetic.main.activity_digital_payment.*
import kotlinx.android.synthetic.main.content_digital_payment.*
import java.util.ArrayList

class DigitalPaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_digital_payment)
        setSupportActionBar(toolbar)

        var data = ArrayList<GridItem>()
        data.add(GridItem(R.drawable.ic_pln, resources.getString(R.string.pln)))
        data.add(GridItem(R.drawable.ic_cellular, resources.getString(R.string.cellular)))
        data.add(GridItem(R.drawable.ic_datapackage, resources.getString(R.string.data_package)))
        data.add(GridItem(R.drawable.ic_postpaid, resources.getString(R.string.postpaid)))
        data.add(GridItem(R.drawable.ic_bpjs, resources.getString(R.string.bpjs)))
        data.add(GridItem(R.drawable.ic_tvcable, resources.getString(R.string.cable_tv)))
        data.add(GridItem(R.drawable.ic_insurance, resources.getString(R.string.insurance)))
        data.add(GridItem(R.drawable.ic_donation, resources.getString(R.string.donation)))

//        recyclerView.adapter = PaymentGridAdapter(data)
        var adapter = PaymentGridViewAdapter(this, data)
        gridView.numColumns = 2
        gridView.adapter = adapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
