package com.hn_2452.shoes_nike.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class OrderDetail(
    @SerializedName("_id")
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("shoes_id")
    val shoes: Shoes,
    val quantity: Int = -1,
    val color: String = "",
    val size: Int = -1,
    val ordered: Boolean = false,
    val evaluated: Int = 3, // 0: yet, 1: not yet, 2: pending, 3: not able
    val star: Float = 0F,
    val comment: String = "",
    @SerializedName("evaluate_time")
    val evaluateTime: Long = -1
) : Parcelable

