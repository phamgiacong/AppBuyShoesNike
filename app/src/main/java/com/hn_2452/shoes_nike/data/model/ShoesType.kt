package com.hn_2452.shoes_nike.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ShoesType(
    @SerializedName("_id")
    val id: String = "",
    val name: String = "",
    @SerializedName("created_date")
    val createdDate: Long = -1
) : Serializable