package com.mili.housead.utilities

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecoration(
    context: Context,
    spacingId: Int
) : RecyclerView.ItemDecoration() {
    private val spacing: Int = context.resources.getDimensionPixelSize(spacingId)
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val layoutManager = parent.layoutManager ?: return
        val position = parent.getChildAdapterPosition(view)
        outRect.apply {
            when (layoutManager) {
                is GridLayoutManager -> {
                    val spanCount = layoutManager.spanCount
                    val column = position % spanCount
                    left = column * spacing / spanCount
                    right = spacing - (column+1) * spacing / spanCount
                    if (position >= spanCount ){
                        top = spacing
                    }
                }
                is LinearLayoutManager ->{
                    if (layoutManager.orientation == RecyclerView.VERTICAL) {
                        if (position > 0) {
                            top = spacing
                        }
                    } else {
                        if (position > 0) {
                            if (layoutManager.reverseLayout) {
                                right = spacing
                            }else{
                                left = spacing
                            }
                        }
                    }
                }
            }
        }
    }
}