package com.solarnet.demo.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.AbsListView
import com.solarnet.demo.MainActivity
import com.solarnet.demo.fragment.ChatFragment
import com.solarnet.demo.fragment.PaymentFragment
import com.solarnet.demo.fragment.StoryFragment

class MainPagerAdapter(fragmentManager: FragmentManager,
                       val mOnScrollListener : MainActivity.OnScrollListener) :
        SmartFragmentStatePagerAdapter(fragmentManager) {

    companion object {
        private val NUM_ITEMS = 3
        const val PAGE_PAYMENT = 0
        const val PAGE_CHAT = 1
        const val PAGE_STORY = 2
    }


    // Returns total number of pages
    override fun getCount(): Int {
        return NUM_ITEMS
    }

    // Returns the fragment to display for that page
    override fun getItem(position: Int): Fragment? {
        when (position) {
            PAGE_PAYMENT // Fragment # 0 - This will show FirstFragment
            -> {
                var fragment = PaymentFragment.newInstance()
                fragment.setOnScrollListener(mOnScrollListener)
                return fragment
            }
            PAGE_CHAT // Fragment # 0 - This will show SecondFragment
            -> {
                var f = ChatFragment.newInstance()
                f.setOnScrollListener(mOnScrollListener)
                return f
            }
            PAGE_STORY // Fragment # 1 - This will show ThirdFragment
            -> return StoryFragment.newInstance()
            else -> return null
        }
    }

    // Returns the page title for the top indicator
    override fun getPageTitle(position: Int): CharSequence? {
        return "Page $position"
    }


}