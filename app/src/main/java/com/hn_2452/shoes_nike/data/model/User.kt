package com.hn_2452.shoes_nike.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("_id")
    var id: String,

    var name: String,
    var orders: List<String> = emptyList(),

    @SerializedName("favorite_shoes")
    var favoriteShoes: List<String> = emptyList(),
    var email: String?,

    @SerializedName("account_type")
    var accountType: Int, // 0: local, 1: facebook, 2: google
    @SerializedName("account_id")
    var accountId: String?,
    var state: Int, // 0: inactive, 1: active, 2: block
    var avatar: String?,
    var offers: List<String> = emptyList(),

    @SerializedName("created_date")
    var createdDate: Long,

    @SerializedName("phone_number")
    var phoneNumber: String? = null
)

