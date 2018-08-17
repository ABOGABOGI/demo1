package com.solarnet.demo.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.solarnet.demo.R
import kotlinx.android.synthetic.main.item_story.view.*
import android.content.Context

class TopUserAdapter(val mContext : Context) : RecyclerView.Adapter<TopUserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(
                parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_itemtopuser, parent, false))
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    inner class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        val peopleImage : ImageView = itemView.findViewById(R.id.peopleImage)
    }
}