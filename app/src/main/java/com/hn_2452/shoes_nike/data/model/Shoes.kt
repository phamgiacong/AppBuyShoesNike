package com.hn_2452.shoes_nike.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Shoes(
    var _id:String,
    var name:String,
    var description: String,
    var price: Long,
    var type:String,
    var rate: Float,
    var sold:Int,
    var available_sizes :List<Int>,
    var available_colors:List<String>,
    var main_image:String,
    var images:List<String>,
    var gender :Int, // 0: All, 1: Male, 2: Female
    var created_date:Long,
    var state:Int, // 0: Inactive, 1: Active, 2: Sold
    val quantity: Long,
    @SerializedName("discount_quantity")
    val discount: Long,
    @SerializedName("discount_unit")
    val discountUnit: Int  // 0: %, 1: vnđ
):Serializable
