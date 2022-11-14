package com.why.lib_base.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView间距
 * @author why
 * @date 2022/11/14 17:01
 */
class ListItemDecoration(private var space: Int = 0) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        val viewLayoutPosition = layoutParams.viewLayoutPosition
        val adapter = parent.adapter as RecyclerView.Adapter

        adapter.apply {
            when (viewLayoutPosition) {
                0 -> {
                    outRect.left = 4 * space
                    outRect.right = space
                }

                itemCount - 1 -> {
                    outRect.left = space
                    outRect.right = 4 * space
                }
                else -> {
                    outRect.left = space
                    outRect.right = space
                }
            }
        }
    }
}