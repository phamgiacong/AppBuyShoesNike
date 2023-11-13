package com.hn_2452.shoes_nike.data.model

import java.io.Serializable

data class ShoesToCart(
    var _id :String,
    var idU :String,
    var idShoes :String,
    var colorChoose:String,
    var sizeChoose:Int,
    var createdDate :Long,
    var quantity:Int
):Serializable
