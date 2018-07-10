package com.solarnet.demo.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.solarnet.demo.R
import com.solarnet.demo.data.GridItem

class PaymentGridAdapter(data : List<GridItem>) : RecyclerView.Adapter<PaymentGridAdapter.ViewHolder>() {
    private val mData : List<GridItem> = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.item_payment_grid, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        holder.icon.setImageResource(item.iconResource)
        holder.text.text = item.text
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon : ImageButton = itemView.findViewById<ImageButton>(R.id.itemIcon)
        val text : TextView = itemView.findViewById<TextView>(R.id.itemText)
    }

}