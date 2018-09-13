package com.solarnet.demo.activity.payment

import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.solarnet.demo.R
import com.solarnet.demo.adapter.PaymentGridViewAdapter
import com.solarnet.demo.adapter.SlidingImageAdapter
import com.solarnet.demo.data.GridItem
import com.solarnet.demo.design.ImageModel
import com.viewpagerindicator.CirclePageIndicator

import kotlinx.android.synthetic.main.activity_digital_payment.*
import kotlinx.android.synthetic.main.content_digital_payment.*
import java.util.*

class DigitalPaymentActivity : AppCompatActivity() {

    private var imageModelArrayList: ArrayList<ImageModel>? = null

    private val myImageList = intArrayOf(R.drawable.lion_sample,R.drawable.lion_sample)

    private var url1: String? = "https://ecs7.tokopedia.net/img/banner/2018/9/12/25618007/25618007_168ebaa8-791d-4c65-a92f-f190a3307961.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_digital_payment)
        setSupportActionBar(toolbar)

        imageModelArrayList = ArrayList()
        imageModelArrayList = populateList()

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

        initilize()

    }

    private fun initilize() {
        mPager = findViewById(R.id.digital_pager) as ViewPager
        mPager!!.adapter = SlidingImageAdapter(this@DigitalPaymentActivity, this.imageModelArrayList!!)

        val indicator = findViewById(R.id.indicator) as CirclePageIndicator

        indicator.setViewPager(mPager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.setRadius(2 * density)

        NUM_PAGES = imageModelArrayList!!.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPager!!.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })
    }

    companion object {

        private var mPager: ViewPager? = null
        private var currentPage = 0
        private var NUM_PAGES = 0
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

    private fun populateList(): ArrayList<ImageModel> {

        val list = ArrayList<ImageModel>()

        for (i in 0..1) {
            val imageModel = ImageModel()
            imageModel.setImage_drawables(myImageList[i])
            list.add(imageModel)
        }

        return list
    }



}
