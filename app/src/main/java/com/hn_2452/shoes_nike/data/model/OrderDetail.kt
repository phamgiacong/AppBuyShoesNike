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
    val size: Int = -1
) : Parcelable

