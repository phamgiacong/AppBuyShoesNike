package com.hn_2452.shoes_nike.data.adapter

import android.util.Log
import com.hn_2452.shoes_nike.MESSAGE
import com.hn_2452.shoes_nike.SUCCESS
import org.json.JSONObject

abstract class BaseAdapter {

    companion object {
        private const val TAG = "Nike:BaseAdapter: "
    }
        
    open fun<T> parseToData(jsonObject: JSONObject, block : () -> T): T {
        Log.i(TAG, "networkDataToLocalData: ")
        val success = jsonObject.getBoolean(SUCCESS)
        if (success) {
            return block()
        }
        throw Exception(jsonObject.getString(MESSAGE))
    }

}