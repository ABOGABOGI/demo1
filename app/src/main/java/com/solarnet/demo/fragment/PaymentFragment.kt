package com.solarnet.demo.fragment

import android.support.v4.app.Fragment
import android.widget.TextView
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.solarnet.demo.R
import com.solarnet.demo.adapter.PaymentGridAdapter
import com.solarnet.demo.data.GridItem


class PaymentFragment : Fragment() {
    private val NUM_COLUMNS = 3
    private lateinit var recyclerView : RecyclerView
    // Store instance variables based on arguments passed
    // newInstance constructor for creating fragment with arguments
    companion object {
        fun newInstance(): PaymentFragment {
            return PaymentFragment()
        }
    }

    // Inflate the view for the fragment based on layout XML
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.recylerView)
        recyclerView.layoutManager = GridLayoutManager(context, NUM_COLUMNS)
        var data = ArrayList<GridItem>()
        data.add(GridItem(R.drawable.ic_send_money, context!!.resources.getString(R.string.send_money)))
        data.add(GridItem(R.drawable.ic_cellular, context!!.resources.getString(R.string.cellular)))
        data.add(GridItem(R.drawable.ic_pln, context!!.resources.getString(R.string.pln)))
        data.add(GridItem(R.drawable.ic_invoice, context!!.resources.getString(R.string.invoice)))
        data.add(GridItem(R.drawable.ic_topup, context!!.resources.getString(R.string.top_up)))
        recyclerView.adapter = PaymentGridAdapter(data)

        return view
    }
}