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
import com.solarnet.demo.data.contact.Contact
import android.text.method.TextKeyListener.clear
import android.util.Log
import java.util.*


class TopUpListAdapter(context : Context, data : ArrayList<Item> = ArrayList<Item>()) :
        RecyclerView.Adapter<TopUpListAdapter.ViewHolder>() {

    companion object {
        const val TYPE_MANUAL_TRANSFER = 0
        const val TYPE_VIRTUAL_ACCOUNT = 1
    }
    class Item(val iconId : Int, val text : String, val info : Int)

    private var mData : ArrayList<Item> = data
    private val mContext : Context = context
    var listener : OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.item_topup_list, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        holder.icon.setImageResource(item.iconId)
        holder.text.text = item.text
        if (item.info == TYPE_MANUAL_TRANSFER) {
            holder.info.setText(R.string.manual_transfer)
        } else {
            holder.info.setText(R.string.virtual_account)
        }
        holder.cardView.tag = position
        holder.cardView.setOnClickListener{v ->
            listener?.onClick(v.tag as Int)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon : ImageView = itemView.findViewById(R.id.imageIcon)
        val text : TextView = itemView.findViewById(R.id.textBank)
        val info : TextView = itemView.findViewById(R.id.textInfo)
        val cardView : CardView = itemView as CardView
    }

    interface OnClickListener {
        fun onClick(position : Int)
    }

}