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
    @SerializedName("favorite_shoes")
    var favoriteShoes: List<String> = emptyList(),
    var email: String?,

    @SerializedName("account_type")
    var accountType: Int, // 0: local, 1: facebook, 2: google
    @SerializedName("account_id")
    var accountId: String?,
    var state: Int, // 0: inactive, 1: active, 2: block
    var avatar: String?,
    @SerializedName("created_date")
    var createdDate: Long,

    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    @SerializedName("full_name")
    val fullName: String? = null,
    @SerializedName("birthday")
    val birthDay: Long? = null,
    @SerializedName("gender")
    val gender: Int? = null
)

