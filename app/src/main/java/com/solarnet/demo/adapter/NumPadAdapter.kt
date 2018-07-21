package com.solarnet.demo.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.solarnet.demo.R


class NumPadAdapter : RecyclerView.Adapter<NumPadAdapter.ViewHolder>() {
    interface OnNumPadListener {
        fun onNumPadKey(key : String)
        fun onNumPadBackspace()
    }

    var listener : OnNumPadListener? = null

    private val BACKSPACE_POSITION = 11
    private val mData : Array<String> = arrayOf(
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "00", "0", "⌫"
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.item_numpad_grid, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = mData[position]
        holder.text.tag = position
        holder.text.setOnClickListener { view ->
            val position = view.tag as Int
            if (position == BACKSPACE_POSITION) {
                listener?.onNumPadBackspace()
            } else {
                listener?.onNumPadKey((view as TextView).text.toString())
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text : TextView = itemView.findViewById(R.id.itemText)
    }

}