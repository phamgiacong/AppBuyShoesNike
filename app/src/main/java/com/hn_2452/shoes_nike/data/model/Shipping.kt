package com.hn_2452.shoes_nike.data.model

import android.os.Parcelable
import java.io.Serializable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Shipping(
    var _id:String,
    var name: String,
    var price: Double?,
    var days:Int
):Parcelable{

}