package com.hn_2452.shoes_nike.data.model

import com.google.gson.annotations.SerializedName

data class UserOffer(
    @SerializedName("_id")
    val id: String,

    @SerializedName("user_id")
    val user: User,

    @SerializedName("offer_id")
    val offer: Offer,

    @SerializedName("used")
    val used: Boolean
)
