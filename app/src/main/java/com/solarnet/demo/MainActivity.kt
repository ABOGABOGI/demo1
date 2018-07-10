package com.solarnet.demo

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.solarnet.demo.adapter.MainPagerAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var adapterViewPager: MainPagerAdapter

    private val onClickNavigationButton = View.OnClickListener { view ->
        when (view.id) {
            R.id.buttonPayment -> {
                setNavigationButton(true, false, false)
                viewPager.currentItem = 0
            }
            R.id.buttonChat -> {
                setNavigationButton(false, true, false)
                viewPager.currentItem = 1
            }
            R.id.buttonStory -> {
                setNavigationButton(false, false, true)
                viewPager.currentItem = 2
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        setNavigationButton(true, false, false)

        buttonPayment.setOnClickListener(onClickNavigationButton)
        buttonChat.setOnClickListener(onClickNavigationButton)
        buttonStory.setOnClickListener(onClickNavigationButton)

        adapterViewPager = MainPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapterViewPager
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                when (position) {
                    MainPagerAdapter.PAGE_PAYMENT ->  { textPage.setText(R.string.title_payment) }
                    MainPagerAdapter.PAGE_CHAT -> textPage.setText(R.string.title_chat)
                    MainPagerAdapter.PAGE_STORY -> textPage.setText(R.string.title_story)
                    else -> textPage.text = ""
                }
            }
        })

    }

    private fun setNavigationButton(enablePayment : Boolean,
                                    enableChat : Boolean,
                                    enableStory : Boolean) {
        if (enablePayment) {
            buttonPayment.setColorFilter(Color.WHITE)
        } else {
            buttonPayment.setColorFilter(Color.GRAY)
        }

        if (enableChat) {
            buttonChat.setColorFilter(Color.WHITE)
        } else {
            buttonChat.setColorFilter(Color.GRAY)
        }

        if (enableStory) {
            buttonStory.setColorFilter(Color.WHITE)
        } else {
            buttonStory.setColorFilter(Color.GRAY)
        }
    }
}
