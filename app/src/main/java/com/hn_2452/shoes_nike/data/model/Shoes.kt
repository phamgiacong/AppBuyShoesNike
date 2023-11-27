package com.hn_2452.shoes_nike.data.model

import java.io.Serializable

data class Shoes(
    var _id:String,
    var name:String,
    var description: String,
    var price: Double,
    var type:String,
    var rate: Float,
    var sold:Int,
    var available_sizes :List<Int>,
    var available_colors:List<String>,
    var main_image:String,
    var images:List<String>,
    var gender :Int,
    var created_date:Long,
    var state:Int
):Serializable
