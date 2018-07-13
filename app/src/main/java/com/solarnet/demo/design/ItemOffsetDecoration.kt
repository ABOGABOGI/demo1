package com.solarnet.demo.design

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.annotation.DimenRes
import android.support.annotation.NonNull
import android.view.View


class ItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

    constructor(context: Context,
                @DimenRes itemOffsetId: Int) :
            this(context.resources.getDimensionPixelSize(itemOffsetId))

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,

                       state: RecyclerView.State) {

        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)

        // no position, leave it alone
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }

        // first item
        if (itemPosition == 0) {
            outRect.set(view.paddingLeft, mItemOffset + view.paddingTop, view.paddingRight, view.paddingBottom);
        }
    }

}