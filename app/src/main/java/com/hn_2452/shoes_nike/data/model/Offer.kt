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
    val image: String,
    val title: String,
    val description: String,
    @SerializedName("max_value")
    val maxDiscount: Long,
    @SerializedName("value_to_apply")
    val valueToApply: Long,
) : Parcelable
