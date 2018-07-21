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


class ContactListAdapter(context : Context, data : ArrayList<Contact> = ArrayList<Contact>()) :
        RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {

    private var mData : ArrayList<Contact> = data
    private var mAllData : List<Contact> = data.clone() as List<Contact>
    private val mContext : Context = context
    var listener : OnClickListener? = null

    fun setData(data : ArrayList<Contact>) {
        mData = data
        mAllData = data.clone() as List<Contact>
    }

    fun removeAll() {
        mData.clear()
        mAllData = ArrayList()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.item_contact_list, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        if (item.image != null) {
//            holder.icon.setImageDrawable(BitmapDrawable(mContext.resources, item.icon))
        }
        holder.text.text = item.name
        holder.cardView.tag = item
        holder.cardView.setOnClickListener{v ->
            val contact = v.tag as Contact
            listener?.onClick(contact)

        }
    }

    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        mData.clear()
        if (charText.isEmpty()) {
            mData.addAll(mAllData)
        } else {
            for (s in mAllData) {
                Log.i("Test", "Name: " + s.name)
                if (s.name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    mData.add(s)
                }
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon : ImageView = itemView.findViewById<ImageView>(R.id.itemIcon)
        val text : TextView = itemView.findViewById<TextView>(R.id.itemText)
        val cardView : CardView = itemView as CardView
    }

    interface OnClickListener {
        fun onClick(contact : Contact)
    }

}