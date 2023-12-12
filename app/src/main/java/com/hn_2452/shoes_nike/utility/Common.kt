package com.hn_2452.shoes_nike.utility

import android.content.Context
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun pxToDp(context: Context, number: Int): Int {
    return (number / context.resources.displayMetrics.density).toInt()
}

fun dpToPx(context: Context, number: Int): Int {
    return (number * context.resources.displayMetrics.density).toInt();
}

fun Long.toDayString(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    return format.format(date)
}

fun Long.toTimeString(): String {
    val seconds = this / 1000
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

fun Long.toVND(): String {
    return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(this)
}

fun String.isNotEmail() : Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    return !this.matches(emailRegex.toRegex())
}

fun String.isNotPassword(): Boolean {
    val passwordRegex = Regex("^(?=.*[A-Z!@#\$%^&*()-_=+\\[\\]{};:'\",<.>/?]).{6,}$")
    return !this.matches(passwordRegex)
}

fun getTimeOfDay(): String {
    val currentTime = Calendar.getInstance()
    val hour = currentTime.get(Calendar.HOUR_OF_DAY)

    return when (hour) {
        in 6..11 -> "Chào buổi sáng"
        in 12..17 -> "Chào buổi chiều"
        else -> "Chào buổi tối"
    }
}