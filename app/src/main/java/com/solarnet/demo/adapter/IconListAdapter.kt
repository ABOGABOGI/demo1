package com.solarnet.demo.adapter

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.view.ViewGroup
import android.widget.ImageView
import com.solarnet.demo.R
import java.util.*
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.view.LayoutInflater






class IconListAdapter<Item>(context: Context?,
                            var items: Array<IconListAdapter.Item>?) :
        ArrayAdapter<IconListAdapter.Item>(context, R.layout.icon_list_item) {
    private lateinit var holder: ViewHolder

    internal inner class ViewHolder {
        var icon: ImageView? = null
        var text: TextView? = null
    }

    class Item(var iconRes : Int, var text : String, var code : String = "")
    override fun getCount() : Int {
        if (items != null) return items!!.count()
        return 0
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //Use super class to create the View
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = convertView
        if (view == null) {
            view = inflater.inflate(
                    R.layout.icon_list_item, null)

            holder = ViewHolder()
            holder?.icon = view!!
                    .findViewById(R.id.icon) as ImageView
            holder?.text = view!!
                    .findViewById(R.id.text) as TextView
            view.tag = holder
        } else {
            // view already defined, retrieve view holder
            holder = view.tag as IconListAdapter<Item>.ViewHolder
        }

        holder.text!!.text = items!![position].text!!
        holder.icon!!.setImageResource(items!![position].iconRes!!)

        return view
    }
}