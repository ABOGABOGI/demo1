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
import com.solarnet.demo.data.product.Product
import com.solarnet.demo.data.util.Utils

class ProductListAdapter(context : Context, data : List<Product> = ArrayList<Product>()) :
        RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    private var mData : List<Product> = data
    private val mContext : Context = context

    var listener : OnClickListener? = null

    fun setData(data : List<Product>) {
        mData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.item_product_list, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        holder.description.text = item.description
        holder.price.text = Utils.currencyString(item.price)
        holder.cardView.tag = item
        holder.cardView.setOnClickListener{v ->
//            Toast.makeText(mContext, "On click: $item.text", Toast.LENGTH_SHORT).show()
            listener?.onClick(v.tag as Product)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description : TextView = itemView.findViewById(R.id.itemDescription)
        val price : TextView = itemView.findViewById(R.id.itemPrice)
        val cardView : CardView = itemView.findViewById(R.id.cardView)
    }

    interface OnClickListener {
        fun onClick(product : Product)
    }


}