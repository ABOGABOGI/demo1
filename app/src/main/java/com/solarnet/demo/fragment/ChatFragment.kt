package com.solarnet.demo.fragment

import android.support.v4.app.Fragment
import android.widget.TextView
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.solarnet.demo.MainActivity
import com.solarnet.demo.R
import com.solarnet.demo.adapter.ChatListAdapter
import com.solarnet.demo.adapter.PaymentGridAdapter
import com.solarnet.demo.data.GridItem
import com.solarnet.demo.data.chat.ChatList
import com.solarnet.demo.design.ItemOffsetDecoration

class ChatFragment : Fragment() {
    private var mOnScrollListener : MainActivity.OnScrollListener? = null
    private var mScrollY : Int = 0

    // Store instance variables based on arguments passed
    // newInstance constructor for creating fragment with arguments
    companion object {
        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }

    fun setOnScrollListener(onScrollListener: MainActivity.OnScrollListener) {
        mOnScrollListener = onScrollListener
    }

    // Inflate the view for the fragment based on layout XML
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        var data = ArrayList<ChatList>()
        data.add(ChatList(null, "Contact 1", "Message 1"))
        data.add(ChatList(null, "Contact 2", "Message 1"))
        data.add(ChatList(null, "Contact 3", "Message 1"))
        data.add(ChatList(null, "Contact 4", "Message 1"))
        data.add(ChatList(null, "Contact 5", "Message 1"))
        data.add(ChatList(null, "Contact 6", "Message 1"))
        data.add(ChatList(null, "Contact 7", "Message 1"))
        data.add(ChatList(null, "Contact 1", "Message 1"))
        data.add(ChatList(null, "Contact 2", "Message 1"))
        data.add(ChatList(null, "Contact 3", "Message 1"))
        data.add(ChatList(null, "Contact 4", "Message 1"))
        data.add(ChatList(null, "Contact 5", "Message 1"))
        data.add(ChatList(null, "Contact 6", "Message 1"))
        data.add(ChatList(null, "Contact 7", "Message 1"))

        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(ItemOffsetDecoration(context!!, R.dimen.nav_height))
        var adapter = ChatListAdapter(context!!, data)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mScrollY += dy
                mOnScrollListener?.onScrollChange(mScrollY, dy)
            }

        })
        return view
    }
}