package com.solarnet.demo.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.solarnet.demo.data.GridItem
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.ImageView
import com.solarnet.demo.R


class PaymentGridViewAdapter : BaseAdapter {
    private var mData: List<GridItem>
    private var mContext : Context

    constructor(context : Context, mData: List<GridItem>) : super() {
        this.mData = mData
        this.mContext = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = mData[position]
        var view = convertView
        if (view == null) {
            val layoutInflater = LayoutInflater.from(mContext)
            view = layoutInflater.inflate(R.layout.item_payment_grid, null)
        }

        val icon = view!!.findViewById<ImageView>(R.id.itemIcon)
        val text = view!!.findViewById<TextView>(R.id.itemText)

        icon?.setImageResource(item.iconResource)
        text?.text = item.text

        return view!!
    }

    override fun getItem(position: Int): GridItem {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return mData.size
    }
}