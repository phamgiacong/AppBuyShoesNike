package com.hn_2452.shoes_nike.utility

import android.content.Context

fun saveStringDataByKey(context: Context, key: String, data: String) {
    val sharedPref = context.getSharedPreferences("DEFAULT", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString(key, data)
        apply()
    }
}

fun getStringDataByKey(context: Context, key: String): String {
    val sharedPref = context.getSharedPreferences("DEFAULT", Context.MODE_PRIVATE)
    return sharedPref.getString(key, "") ?: ""
}

fun saveBooleanDataByKey(context: Context, key: String, data: Boolean) {
    val sharedPref = context.getSharedPreferences("DEFAULT", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putBoolean(key, data)
        apply()
    }
}

fun getBooleanDataByKey(context: Context, key: String): Boolean {
    val sharedPref = context.getSharedPreferences("DEFAULT", Context.MODE_PRIVATE)
    return sharedPref.getBoolean(key, false) ?: false
}