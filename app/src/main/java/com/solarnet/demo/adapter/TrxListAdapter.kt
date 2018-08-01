package com.solarnet.demo.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.solarnet.demo.R
import com.solarnet.demo.activity.TrxActivity
import com.solarnet.demo.data.trx.Trx
import com.solarnet.demo.data.util.Utils
import kotlinx.android.synthetic.main.item_trx_list.view.*

class TrxListAdapter(context : Context, data : List<Trx>) :
        RecyclerView.Adapter<TrxListAdapter.ViewHolder>() {

    private var mData : List<Trx> = data
    private val mContext : Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.item_trx_list, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = mData[position]
//        if (item.icon != null) {
//            holder.icon.setImageDrawable(BitmapDrawable(mContext.resources, item.icon))
//        }
//        holder.text.text = item.contact
//        holder.smallText.text = item.message
        holder.cardView.tag = position
        holder.cardView.setOnClickListener{v ->
            val pos = v.tag as Int
            mContext.startActivity(Intent(mContext, TrxActivity::class.java).apply {
                putExtra(TrxActivity.EXTRA_TRX_ID, mData[pos].id)
            })
        }
        val item = mData[position]
        holder.title.text = item.title
        holder.amount.text = Utils.currencyString(item.amount)
        holder.message.text = item.message
        holder.date.text = "Today"
    }

    fun setItems(data : List<Trx>) {
        mData = data
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon : ImageView = itemView.findViewById(R.id.itemIcon)
        val title : TextView = itemView.findViewById(R.id.itemTitle)
        val message : TextView = itemView.findViewById(R.id.itemMessage)
        val amount : TextView = itemView.findViewById(R.id.itemAmount)
        val date : TextView = itemView.findViewById(R.id.itemDate)
        val cardView : CardView = itemView as CardView
    }


}