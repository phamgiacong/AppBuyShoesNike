package com.hn_2452.shoes_nike.data.adapter

import android.util.Log
import com.hn_2452.shoes_nike.DATA
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.utility.getOrNull
import org.json.JSONObject

class ShoesAdapter : BaseAdapter() {
    companion object {
        private const val TAG = "Nike:ShoesAdapter: "
    }

    fun networkDataToShoesList(jsonObject: JSONObject): List<Shoes> = parseToData(jsonObject) {
        jsonObjectToShoesListNoPage(jsonObject)
    }

    private fun jsonObjectToShoesListNoPage(jsonObject: JSONObject): List<Shoes> {
        Log.i(TAG, "jsonObjectToShoesList: $jsonObject")
        val shoesJSONArray = jsonObject.getJSONArray(DATA)
        val shoesList = mutableListOf<Shoes>()

        for (i in 0 until shoesJSONArray.length()) {
            val shoesJSONObject = shoesJSONArray.getJSONObject(i)
            val id = shoesJSONObject.getOrNull<String>("_id") ?: ""
            val name = shoesJSONObject.getOrNull<String>("name") ?: ""
            val price = shoesJSONObject.getOrNull<Number>("price") ?: -1L
            val rate = shoesJSONObject.getOrNull<Number>("rate") ?: -1F
            val sold = shoesJSONObject.getOrNull<Number>("sold") ?: -1L
            val mainImage = shoesJSONObject.getOrNull<String>("main_image") ?: ""

            shoesList.add(
                Shoes(
                    id = id,
                    name = name,
                    price = price.toLong(),
                    rate = rate.toFloat(),
                    sold = sold.toLong(),
                    mainImage = mainImage
                )
            )
        }
        return shoesList
    }

    private fun jsonObjectToShoes(jsonObject: JSONObject): Shoes {
        Log.i(TAG, "jsonObjectToShoes: $jsonObject")
        val shoesJSONObject = jsonObject.getJSONObject(DATA)

        val id = shoesJSONObject.getOrNull<String>("_id") ?: ""
        val name = shoesJSONObject.getOrNull<String>("name") ?: ""
        val description = shoesJSONObject.getOrNull<String>("description") ?: ""
        val price = shoesJSONObject.getOrNull<Number>("price") ?: -1L
        val type = shoesJSONObject.getOrNull<String>("type") ?: ""
        val rate = shoesJSONObject.getOrNull<Number>("rate") ?: -1F
        val sold = shoesJSONObject.getOrNull<Number>("sold") ?: -1L
        val mainImage = shoesJSONObject.getOrNull<String>("main_image") ?: ""

        val availableSize = mutableListOf<Int>()
        val availableSizeJSONArray = shoesJSONObject.getJSONArray("available_sizes")
        for (i in 0 until availableSizeJSONArray.length()) {
            availableSize.add(availableSizeJSONArray.getInt(i))
        }

        val availableColor = mutableListOf<String>()
        val availableColorJSONArray = shoesJSONObject.getJSONArray("available_colors")
        for (i in 0 until availableColorJSONArray.length()) {
            availableColor.add(availableColorJSONArray.getString(i))
        }

        val images = mutableListOf<String>()
        val imagesJSONArray = shoesJSONObject.getJSONArray("images")
        for (i in 0 until imagesJSONArray.length()) {
            images.add(imagesJSONArray.getString(i))
        }

        val gender = shoesJSONObject.getOrNull<Int>("gender") ?: -1
        val state = shoesJSONObject.getOrNull<Int>("state") ?: -1
        val createdDate = shoesJSONObject.getOrNull<Long>("created_date") ?: -1L

        return Shoes(
            id = id,
            name = name,
            description = description,
            shoesType = type,
            rate = rate.toFloat(),
            sold = sold.toLong(),
            availableColor = availableColor,
            availableSize = availableSize,
            images = images,
            gender = gender,
            createdDate = createdDate,
            state = state,
            price = price.toLong(),
            mainImage = mainImage
        )
    }

}