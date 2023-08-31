package com.mili.housead.utilities


import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class PaginationListener(private val layoutManager: GridLayoutManager,
                                  private val pageCount:Int,
                                  private val perPage:Int) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if ( !isLoading()&& !isLastPage()) {
            if (
                visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0 && totalItemCount >= perPage
            ) {
                loadMoreItems()
            }
        }
    }

    abstract fun loadMoreItems():Unit

    abstract fun isLastPage():Boolean

    abstract fun isLoading():Boolean

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }
}