package com.kbline.kotlin_module.RecyclerUtil

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpacesItemDecoration(private val space: Int, private val row: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position >= 0) {
            if (row == 1) {
                val column = position % row
                if (position < row) {
                    outRect.top = space
                }
                outRect.bottom = space
            } else {
                val column = position % row
                outRect.left = space - space * column / row
                outRect.right = (column + 1) * space / row
                if (position < row) {
                    outRect.top = space
                }
                outRect.bottom = space
            }
            return
        }
        outRect.left = 0
        outRect.right = 0
        outRect.top = 0
        outRect.bottom = 0
    }

}