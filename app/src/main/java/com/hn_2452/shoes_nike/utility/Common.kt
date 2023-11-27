package com.hn_2452.shoes_nike.utility

import android.content.Context
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun pxToDp(context: Context, number: Int): Int {
    return (number / context.resources.displayMetrics.density).toInt()
}

fun dpToPx(context: Context, number: Int): Int {
    return (number * context.resources.displayMetrics.density).toInt();
}

fun Long.toTimeString(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    return format.format(date)
}

fun Long.toVND(): String {
    return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(this)
}