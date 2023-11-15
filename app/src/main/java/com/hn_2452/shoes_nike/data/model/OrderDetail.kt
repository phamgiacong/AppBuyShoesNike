package com.hn_2452.shoes_nike.data.model

data class OrderDetail(
    val shoesId: String = "",
    var totalPrice: Long = -1,
    var numberOfShoes: Int = -1,
    var color: String = "",
    var size: Int = -1
)