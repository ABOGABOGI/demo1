package com.solarnet.demo.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.solarnet.demo.R
import com.solarnet.demo.data.bill.BillProduct
import com.solarnet.demo.data.product.Product
import com.solarnet.demo.data.util.DecimalDigitFilter
import com.solarnet.demo.data.util.ListTextWatcher
import com.solarnet.demo.data.util.Utils

class BillProductListAdapter(context : Context) :
        RecyclerView.Adapter<BillProductListAdapter.ViewHolder>() {

    private var mData : List<BillProduct> =  ArrayList<BillProduct>()
    private val mContext : Context = context

    var listener : OnSubtotalChangedListener? = null

    fun setData(data : List<Product>) {
        var list = ArrayList<BillProduct>()
        data.forEach{dt ->
            list.add(BillProduct(dt))
        }
        mData = list
        notifyDataSetChanged()
    }

    fun getData() : List<BillProduct> {
        return mData
    }

    fun notifyItemQuantityChanged() {
        var subtotal : Int = 0
        Log.i("Test", "-------------- Start: $subtotal")
        mData.forEach{billProduct ->
            subtotal += billProduct.product.price * billProduct.quantity
            if (billProduct.quantity > 0) {
                Log.i("Test","${billProduct.product.description} : ${billProduct.quantity}")
            }
        }
        Log.i("Test", "-------------- End: $subtotal")
        listener?.onSubtotalChanged(subtotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.item_bill_product_list, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        holder.description.text = item.product.description
        holder.price.text = Utils.currencyString(item.product.price)
        holder.cardView.tag = item
        holder.add.tag = position
        holder.deduct.tag = position
        holder.quantity.tag = position
        holder.quantity.filters = arrayOf(DecimalDigitFilter())

        holder.add.setOnClickListener{v ->
            val pos = v.tag as Int
            mData[pos].quantity++
            Log.i("Test", "ADD $pos : ${mData[pos].quantity}")
            notifyItemQuantityChanged()
            notifyItemChanged(pos)
        }


        holder.deduct.setOnClickListener{v ->
            val pos = v.tag as Int
            if (mData[pos].quantity >= 0) {
                mData[pos].quantity--
                notifyItemQuantityChanged()
                notifyItemChanged(pos)
            }
            Log.i("Test", "DEDUCT $pos : ${mData[pos].quantity}")
        }
        holder.quantity.setText(item.quantity.toString())
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description : TextView = itemView.findViewById(R.id.itemDescription)
        val price : TextView = itemView.findViewById(R.id.itemPrice)
        val cardView : CardView = itemView.findViewById(R.id.cardView)
        val add : ImageButton = itemView.findViewById(R.id.buttonAdd)
        val deduct : ImageButton = itemView.findViewById(R.id.buttonDeduct)
        val quantity : EditText = itemView.findViewById(R.id.editQuantity)
    }

    interface OnSubtotalChangedListener {
        fun onSubtotalChanged(subtotal : Int)
    }
}