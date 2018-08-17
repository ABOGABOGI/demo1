package com.solarnet.demo.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.solarnet.demo.R
import kotlinx.android.synthetic.main.item_story.view.*
import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.solarnet.demo.activity.story.PeopleActivity
import com.solarnet.demo.data.story.People

class TopUserAdapter(val mContext : Context,
                     val mData : List<People>) : RecyclerView.Adapter<TopUserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(
                parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_itemtopuser, parent, false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.peopleName.text = data.name
        if (data.picture != null) {
            Glide.with(mContext).load(data.picture).apply(RequestOptions().apply {
                placeholder(R.drawable.profile_default)
                error(R.drawable.profile_default)
            }).into(holder.peopleImage)
        } else {
            // make sure Glide doesn't load anything into this view until told otherwise
            Glide.with(mContext).clear(holder.peopleImage)
            // remove the placeholder (optional); read comments below
            holder.peopleImage.setImageResource(R.drawable.profile_default)
        }

        holder.peopleImage.setOnClickListener { _ ->
            mContext.startActivity(Intent(mContext, PeopleActivity::class.java).apply {
                putExtra(PeopleActivity.EXTRA_NAME, data.name)
                putExtra(PeopleActivity.EXTRA_PICTURE, data.picture)
                putExtra(PeopleActivity.EXTRA_USERNAME, data.username)
            })
        }
    }

    inner class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        val peopleImage : ImageView = itemView.findViewById(R.id.peopleImage)
        val peopleName : TextView = itemView.findViewById(R.id.peopleName)
    }
}