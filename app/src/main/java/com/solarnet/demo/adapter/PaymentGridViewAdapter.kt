package com.solarnet.demo.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.solarnet.demo.data.GridItem
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.ImageView
import com.solarnet.demo.R
import com.solarnet.demo.activity.payment.*


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
        val text = view.findViewById<TextView>(R.id.itemText)

        icon?.setImageResource(item.iconResource)
        text?.text = item.text

        view.tag = item.iconResource
        view.setOnClickListener{v ->
            val iconRes = v.tag as Int
            startActivity(iconRes)
        }

        return view!!
    }

    private fun startActivity(iconRes : Int) {
        when (iconRes) {
            R.drawable.ic_send_money -> {
                mContext.startActivity(Intent(mContext, SendMoneyActivity::class.java))
            }
            R.drawable.ic_cellular -> {
                mContext.startActivity(Intent(mContext, CellularActivity::class.java))
            }
            R.drawable.ic_pln -> {
                mContext.startActivity(Intent(mContext, PLNActivity::class.java))
            }
            R.drawable.ic_invoice -> {
                mContext.startActivity(Intent(mContext, InvoiceActivity::class.java))
            }
            R.drawable.ic_bank -> {
                mContext.startActivity(Intent(mContext, WithdrawActivity::class.java))
            }
            R.drawable.ic_qr -> {
                mContext.startActivity(Intent(mContext, QrActivity::class.java))
            }
        }
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