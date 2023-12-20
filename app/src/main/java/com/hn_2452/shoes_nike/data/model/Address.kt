package com.hn_2452.shoes_nike.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    @SerializedName("_id")
    var id: String,
    @SerializedName("user_name")
    var name: String,
    var address: String,
    var default: Boolean,
    val type: Int,
    @SerializedName("phone_number")
    val phoneNumber: String
) : Parcelable