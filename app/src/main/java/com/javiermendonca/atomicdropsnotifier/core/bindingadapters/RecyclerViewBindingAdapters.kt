package com.javiermendonca.atomicdropsnotifier.core.bindingadapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("loadMoreItems")
fun loadMoreItems(view: RecyclerView, loadMore: () -> Unit) = with(view) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!recyclerView.canScrollVertically(1) && dy > 0) {
                loadMore()
            }
        }
    })
}