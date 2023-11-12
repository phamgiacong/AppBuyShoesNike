package com.hn_2452.shoes_nike.utility

import android.content.Context

fun pxToDp(context: Context, number: Int): Int {
    return (number / context.resources.displayMetrics.density).toInt()
}

fun dpToPx(context: Context, number: Int): Int {
    return (number * context.resources.displayMetrics.density).toInt();
}