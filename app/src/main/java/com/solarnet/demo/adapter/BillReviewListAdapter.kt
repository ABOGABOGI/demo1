package com.solarnet.demo.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.solarnet.demo.R
import com.solarnet.demo.data.bill.BillProduct
import com.solarnet.demo.data.product.Product
import com.solarnet.demo.data.util.DecimalDigitFilter
import com.solarnet.demo.data.util.ListTextWatcher
import com.solarnet.demo.data.util.Utils
import kotlinx.android.synthetic.main.activity_product_qr.*

class BillReviewListAdapter(context : Context, data : List<BillProduct>,
                            private val subtotal : Int ,
                            private val tax: Int) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mData : List<BillProduct> =  data
    private val mContext : Context = context
    private val taxValue = when (tax) {
        0 -> 0
        else -> tax * subtotal / 100
    }
    private val total : Int = subtotal + taxValue
    private var qrCode : String? = null

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_CONTENT = 1
        const val TYPE_SUMMARY = 2
    }


    override fun getItemViewType(position: Int): Int {
        var type = TYPE_CONTENT
        if (position == 0) {
            type = TYPE_HEADER
        } else if (position > mData.size) {
            type = TYPE_SUMMARY
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(
            parent.context)

        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(inflater.inflate(R.layout.item_bill_review0, parent, false))
            TYPE_SUMMARY -> ViewHolder(inflater.inflate(R.layout.item_bill_review2, parent, false))
            else -> ViewHolder(inflater.inflate(R.layout.item_bill_review1, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return mData.size + 4
    }

    fun setJsonQr(code : String) {

        qrCode = code
        notifyItemChanged(0) //update header
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
        position == 0 -> {
            val hold : HeaderViewHolder = holder as HeaderViewHolder
            if (qrCode == null) {
                hold.layoutQr.visibility = View.GONE
            } else {
                hold.layoutQr.visibility = View.VISIBLE
                val bmp = Utils.generateQr(Utils.createBillJsonQR(qrCode!!))
                if (bmp != null) {
                    hold.imageQr.setImageBitmap(bmp)
                }
                holder.billId.text = mContext.resources.getString(R.string.bill_id) + qrCode
                
            }
        }
        position > mData.size -> {
            val index = position - mData.size
            val hold : ViewHolder = holder as ViewHolder
            when (index) {
                1 -> {
                    hold.description.text = mContext.resources.getString(R.string.subtotal)
                    hold.total.text = Utils.currencyString(subtotal)
                }
                2 -> {
                    hold.description.text = mContext.resources.getString(R.string.tax) + " ($tax%)"
                    hold.total.text = Utils.currencyString(taxValue)
                }
                3 -> {
                    hold.description.text = mContext.resources.getString(R.string.total)
                    hold.total.text = Utils.currencyString(total)
                }
            }
        }
        else -> {
            val hold : ViewHolder = holder as ViewHolder
            val item = mData[position - 1]
            hold.description.text = item.product.description
            hold.total.text = Utils.currencyString(item.product.price * item.quantity)
            hold.quantity.text = item.quantity.toString()
            }
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val description : TextView = itemView.findViewById(R.id.textDescription)
            val total : TextView = itemView.findViewById(R.id.textTotal)
            val quantity : TextView = itemView.findViewById(R.id.textQuantity)
    }

    inner class HeaderViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val layoutQr : LinearLayout = itemView.findViewById(R.id.layoutQr)
        val imageQr : ImageView = itemView.findViewById(R.id.imageQr)
        val billId : TextView = itemView.findViewById(R.id.textBill)
    }
}