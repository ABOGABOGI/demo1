package com.solarnet.demo.fragment

import android.support.v4.app.Fragment
import android.widget.TextView
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.solarnet.demo.R
import com.solarnet.demo.adapter.PaymentGridAdapter
import com.solarnet.demo.data.GridItem


class StoryFragment : Fragment() {
    // Store instance variables based on arguments passed
    // newInstance constructor for creating fragment with arguments
    companion object {
        fun newInstance(): StoryFragment {
            return StoryFragment()
        }
    }

    // Inflate the view for the fragment based on layout XML
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_story, container, false)


        return view
    }
}