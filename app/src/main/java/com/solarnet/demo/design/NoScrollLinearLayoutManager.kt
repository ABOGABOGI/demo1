package com.solarnet.demo.design

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class NoScrollLinearLayoutManager(context : Context,
                                  orientation : Int,
                                  reverseLayout : Boolean)  :
        LinearLayoutManager(context, orientation, reverseLayout) {
    private val mMeasuredDimension = IntArray(2)

    override fun isAutoMeasureEnabled(): Boolean {
         return false
    }

    @SuppressLint("SwitchIntDef")
    override fun onMeasure(recycler: RecyclerView.Recycler?, state: RecyclerView.State?,
                           widthSpec: Int, heightSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthSpec)
        val heightMode = View.MeasureSpec.getMode(heightSpec)
        val widthSize = View.MeasureSpec.getSize(widthSpec)
        val heightSize = View.MeasureSpec.getSize(heightSpec)
        var width = 0
        var height = 0

        for (i in 0 until itemCount) {
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                measureScrapChild(recycler!!, i,
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        heightSpec,
                        mMeasuredDimension)

                width = width + mMeasuredDimension[0]
                if (i == 0) {
                    height = mMeasuredDimension[1]
                }
            } else {
                measureScrapChild(recycler!!, i,
                        widthSpec,
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        mMeasuredDimension)
                height = height + mMeasuredDimension[1]
                if (i == 0) {
                    width = mMeasuredDimension[0]
                }
            }
        }
        when (widthMode) {
            View.MeasureSpec.EXACTLY -> width = widthSize
        }

        when (heightMode) {
            View.MeasureSpec.EXACTLY -> height = heightSize
        }

        setMeasuredDimension(width, height)
    }

    private fun measureScrapChild(recycler: RecyclerView.Recycler, position: Int, widthSpec: Int,
                                  heightSpec: Int, measuredDimension: IntArray) {
        val view = recycler.getViewForPosition(position)
        recycler.bindViewToPosition(view, position)
        if (view != null) {
            val p = view.layoutParams as RecyclerView.LayoutParams
            val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                    paddingLeft + paddingRight, p.width)
            val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                    paddingTop + paddingBottom, p.height)
            view.measure(childWidthSpec, childHeightSpec)
            measuredDimension[0] = view.measuredWidth + p.leftMargin + p.rightMargin
            measuredDimension[1] = view.measuredHeight + p.bottomMargin + p.topMargin
            recycler.recycleView(view)
        }
    }
}