package com.scand.internship.mars_scout.utils

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


@BindingAdapter("refreshing")
fun bindRefreshing(refreshView: SwipeRefreshLayout, loading: Boolean?) {
    refreshView.isRefreshing = loading == true
}

