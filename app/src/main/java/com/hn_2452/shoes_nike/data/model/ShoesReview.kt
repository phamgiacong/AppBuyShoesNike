package com.hn_2452.shoes_nike.data.model

import com.google.gson.annotations.SerializedName

data class ShoesReview(
    @SerializedName("_id")
    val id: String,
    val comment: String,
    val rate: Float,
    @SerializedName("user_id")
    val user: User,
    @SerializedName("created_date")
    val postingTime: Long,
    @SerializedName("shoes_id")
    val shoesId: String
)