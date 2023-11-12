package com.hn_2452.shoes_nike.data.adapter

import android.util.Log
import com.hn_2452.shoes_nike.DATA
import com.hn_2452.shoes_nike.data.model.ShoesType
import com.hn_2452.shoes_nike.utility.getOrNull
import org.json.JSONObject

class ShoesTypeAdapter : BaseAdapter() {

    companion object {
        private const val TAG = "Nike:ShoesTypeAdapter: "
    }

    fun networkDataToShoesTypeList(jsonObject: JSONObject): List<ShoesType> =
        parseToData(jsonObject) {
            Log.i(TAG, "networkDataToShoesTypeList: $jsonObject")
            val data = jsonObject.getJSONArray(DATA)
            val shoesTypeList = mutableListOf<ShoesType>()
            for (i in 0 until data.length()) {
                val shoesTypeJson = data.getJSONObject(i)
                val id = shoesTypeJson.getOrNull<String>("_id") ?: ""
                val name = shoesTypeJson.getOrNull<String>("name") ?: ""
                val createdDate = shoesTypeJson.getOrNull<Number>("created_date") ?: -1
                shoesTypeList.add(ShoesType(id, name, createdDate.toLong()))
            }
            shoesTypeList
        }

}