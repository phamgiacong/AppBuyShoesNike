package com.hn_2452.shoes_nike.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable


@Parcelize
data class Offer(
    @SerializedName("_id")
    val id: String,
    val discount: Long,
    @SerializedName("discount_unit")
    val discountUnit: Int, //1: %, 2: VNĐ
    @SerializedName("start_time")
    val startTime: Long,
    @SerializedName("end_time")
    val endTime: Long,
    @SerializedName("applied_product_type")
    val appliedProductType: Int,  // 1: Shoes, 2: Shoes type
    @SerializedName("applied_shoes")
    val appliedShoes: List<String> = emptyList(),
    @SerializedName("applied_shoes_type")
    val appliedShoesType: List<String> = emptyList(),
    @SerializedName("applied_user_type")
    val appliedUserType: List<Int>, //0:All, 1: New, 2: Silver, 3: Golden, 4: Diamond
    val image: String,
    val title: String,
    @SerializedName("sub_title")
    val subTitle: String,
    val description: String,
    @SerializedName("background_image")
    val imageBackground: String,
) : Parcelable, Serializable
