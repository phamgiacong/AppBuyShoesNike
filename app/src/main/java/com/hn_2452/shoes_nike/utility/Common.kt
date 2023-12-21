package com.hn_2452.shoes_nike.utility

import android.content.Context
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import com.hn_2452.shoes_nike.data.model.Shoes
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

fun Long?.toVND(): String {
    if(this == null) return ""
    return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(this)
}

fun String.isNotEmail() : Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    return !this.matches(emailRegex.toRegex())
}

fun String.isNotPassword(): Boolean {
    //(?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
    //(?=\\S+$)          # no whitespace allowed in the entire string
    val passwordRegex = Regex("^(?=.*[@#$%^&+=])(?=\\S+$).{6,}$")
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

fun getPrice(shoes: Shoes): CharSequence {
    return if(shoes.discountUnit == 0) {
        val price = shoes.price - (shoes.price * shoes.discount / 100)
        price.toVND()
    } else {
        val price = shoes.price - shoes.discount
        price.toVND()
    }
}

fun getOriginPrice(shoes: Shoes) : SpannableString {
    val price = shoes.price
    val priceString = SpannableString(price.toVND())
    priceString.setSpan(StrikethroughSpan(), 0, priceString.length, 0)
    return priceString
}