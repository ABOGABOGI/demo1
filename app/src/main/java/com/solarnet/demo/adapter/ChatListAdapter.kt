package com.solarnet.demo.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.solarnet.demo.R
import com.solarnet.demo.data.GridItem
import com.solarnet.demo.data.chat.ChatList

class ChatListAdapter(context : Context, data : List<ChatList>) :
        RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    private val mData : List<ChatList> = data
    private val mContext : Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.item_chat_list, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        if (item.icon != null) {
            holder.icon.setImageDrawable(BitmapDrawable(mContext.resources, item.icon))
        }
        holder.text.text = item.contact
        holder.smallText.text = item.message
        holder.cardView.setOnClickListener{v ->
            Toast.makeText(mContext, "On click: $item.text", Toast.LENGTH_SHORT).show()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon : ImageView = itemView.findViewById<ImageView>(R.id.itemIcon)
        val text : TextView = itemView.findViewById<TextView>(R.id.itemText)
        val smallText : TextView = itemView.findViewById<TextView>(R.id.itemSmallText)
        val cardView : CardView = itemView as CardView
    }


}