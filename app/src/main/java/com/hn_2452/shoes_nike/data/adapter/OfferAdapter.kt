package com.hn_2452.shoes_nike.data.adapter

import android.util.Log
import com.hn_2452.shoes_nike.DATA
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.utility.getOrNull
import org.json.JSONObject

class OfferAdapter : BaseAdapter() {
    companion object {
        const val TAG = "Nike:OfferAdapter: "
    }

    fun networkDataToOfferList(jsonObject: JSONObject): List<Offer> = parseToData(jsonObject) {
        Log.i(TAG, "networkDataToOfferList: $jsonObject")
        val offerJSONArray = jsonObject.getJSONArray(DATA)
        val offerList = mutableListOf<Offer>()

        for (i in 0 until offerJSONArray.length()) {
            val offerJSONObject = offerJSONArray.getJSONObject(i)
            val id = offerJSONObject.getOrNull("_id") ?: ""
            val title = offerJSONObject.getOrNull("title") ?: ""
            val subTitle = offerJSONObject.getOrNull("sub_title") ?: ""
            val description = offerJSONObject.getOrNull("description") ?: ""
            val discount = offerJSONObject.getOrNull<Number>("discount") ?: -1L
            val discountUnit = offerJSONObject.getOrNull("discount_unit") ?: -1
            val startTime = offerJSONObject.getOrNull("start_time") ?: -1L
            val endTime = offerJSONObject.getOrNull("end_time") ?: -1L
            val appliedTypeProduct = offerJSONObject.getOrNull("applied_product_type") ?: -1
            val image = offerJSONObject.getOrNull("image") ?: ""
            val appliedUserTypeJSON = offerJSONObject.getJSONArray("applied_user_type")
            val imageBackground = offerJSONObject.getOrNull("background_image")?: "#e33327"

            val appliedUserType = mutableListOf<Int>()
            if (appliedUserTypeJSON.get(0) == 0) {
                appliedUserType.add(0)
            } else {
                for (j in 0 until appliedUserTypeJSON.length()) {
                    appliedUserType.add(appliedUserTypeJSON.getInt(j))
                }
            }

            if (appliedTypeProduct == 1) {
                val appliedShoesJSON = offerJSONObject.getJSONArray("applied_shoes")
                val appliedShoes = mutableListOf<String>()
                for (k in 0 until appliedShoesJSON.length()) {
                    appliedShoes.add(appliedShoesJSON.getString(k))
                }
                offerList.add(
                    Offer(
                        discount = discount.toLong(),
                        discountUnit = discountUnit,
                        startTime = startTime,
                        endTime = endTime,
                        appliedProductType = appliedTypeProduct,
                        appliedShoes = appliedShoes,
                        appliedUserType = appliedUserType,
                        title = title,
                        subTitle = subTitle,
                        description = description,
                        image = image,
                        id = id,
                        imageBackground = imageBackground
                    )
                )
            } else {
                val appliedShoesTypeJSON = offerJSONObject.getJSONArray("applied_shoes_type")
                val appliedShoesType = mutableListOf<String>()
                for (n in 0 until appliedShoesTypeJSON.length()) {
                    appliedShoesType.add(appliedShoesTypeJSON.getString(n))
                }
                offerList.add(
                    Offer(
                        discount = discount.toLong(),
                        discountUnit = discountUnit,
                        startTime = startTime,
                        endTime = endTime,
                        appliedProductType = appliedTypeProduct,
                        appliedShoesType = appliedShoesType,
                        appliedUserType = appliedUserType,
                        title = title,
                        subTitle = subTitle,
                        description = description,
                        image = image,
                        id = id,
                        imageBackground =imageBackground
                    )
                )


            }

        }
        return@parseToData offerList
    }
}