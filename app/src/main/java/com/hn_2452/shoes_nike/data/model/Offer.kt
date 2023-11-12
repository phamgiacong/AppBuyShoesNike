package com.hn_2452.shoes_nike.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Offer(
    val id : String,
    val discount: Long,
    val discountUnit: Int, //1: %, 2: VNĐ
    val startTime: Long,
    val endTime: Long,
    val appliedProductType: Int,  // 1: Shoes, 2: Shoes type
    val appliedShoes: List<String> = emptyList(),
    val appliedShoesType: List<String> = emptyList(),
    val appliedUserType: List<Int>, //0:All, 1: New, 2: Silver, 3: Golden, 4: Diamond
    val image: String,
    val title: String,
    val subTitle: String,
    val description: String,
    val imageBackground: String,
)  : Parcelable
