package com.solarnet.demo.fragment

import android.support.v4.app.Fragment
import android.widget.TextView
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.GridView
import android.widget.ScrollView
import com.solarnet.demo.MainActivity
import com.solarnet.demo.R
import com.solarnet.demo.adapter.PaymentGridAdapter
import com.solarnet.demo.adapter.PaymentGridViewAdapter
import com.solarnet.demo.data.GridItem
import kotlinx.android.synthetic.main.fragment_payment.*


class PaymentFragment : Fragment() {
    private val NUM_COLUMNS = 4
    private lateinit var recyclerView : RecyclerView
    private var mOnScrollListener : MainActivity.OnScrollListener? = null
    // Store instance variables based on arguments passed
    // newInstance constructor for creating fragment with arguments
    companion object {
        fun newInstance(): PaymentFragment {
            return PaymentFragment()
        }
    }

    fun setOnScrollListener(onScrollListener: MainActivity.OnScrollListener) {
        mOnScrollListener = onScrollListener
    }
    // Inflate the view for the fragment based on layout XML
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)

//        recyclerView = view.findViewById<RecyclerView>(R.id.recylerView)
//        recyclerView.layoutManager = GridLayoutManager(context, NUM_COLUMNS)
//        recyclerView.isNestedScrollingEnabled = false
//        recyclerView.setHasFixedSize(true)
        var data = ArrayList<GridItem>()
        data.add(GridItem(R.drawable.ic_send_money, context!!.resources.getString(R.string.send_money)))
        data.add(GridItem(R.drawable.ic_cellular, context!!.resources.getString(R.string.cellular)))
        data.add(GridItem(R.drawable.ic_pln, context!!.resources.getString(R.string.pln)))
        data.add(GridItem(R.drawable.ic_invoice, context!!.resources.getString(R.string.invoice)))
        data.add(GridItem(R.drawable.ic_bank, context!!.resources.getString(R.string.transfer_to_bank)))
        data.add(GridItem(R.drawable.ic_qr, context!!.resources.getString(R.string.scan_qr)))
        data.add(GridItem(R.drawable.ic_product, context!!.resources.getString(R.string.create_product)))
        data.add(GridItem(R.drawable.ic_agent, context!!.resources.getString(R.string.new_agent)))


//        recyclerView.adapter = PaymentGridAdapter(data)
        var adapter = PaymentGridViewAdapter(context!!, data)
        val gridView = view.findViewById<GridView>(R.id.gridView)
        gridView.numColumns = NUM_COLUMNS
        gridView.adapter = adapter

        //setGridViewHeightBasedOnChildren(gridView, NUM_COLUMNS)

        var scrollView = view as NestedScrollView
        scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener {
            v, scrollX, scrollY, oldScrollX, oldScrollY ->
            mOnScrollListener?.onScrollChange(scrollY, scrollY - oldScrollY)
        })

        return view
    }

    fun setGridViewHeightBasedOnChildren(gridView: GridView, columns: Int) {
        val listAdapter = gridView.adapter
                ?: // pre-condition
                return

        var totalHeight = 0
        val items = listAdapter.count
        var rows = 0

        val listItem = listAdapter.getView(0, null, gridView)
        listItem.measure(0, 0)
        totalHeight = listItem.measuredHeight

        var x = 1f
        if (items > columns) {
            x = (items / columns).toFloat()
            rows = (x + 1).toInt()
            totalHeight *= rows
        }

        val params = gridView.layoutParams
        params.height = totalHeight
        gridView.layoutParams = params

    }
}