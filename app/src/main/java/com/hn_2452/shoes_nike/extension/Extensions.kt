package com.hn_2452.shoes_nike.extension

import android.os.SystemClock
import android.view.View

fun View.setSafeOnClickListener(interval: Long = 1000L, onClick: (View) -> Unit) {
    var lastClickTime = 0L
    this.setOnClickListener { view ->
        val currentItem = SystemClock.elapsedRealtime()
        if (currentItem - lastClickTime >= interval) {
            lastClickTime = currentItem
            onClick(view)
        }
    }
}