package com.hn_2452.shoes_nike.data.model

class Receipt(
    val _id:String?,
    val cart:Cart?,
    val status:Int?,
    val createdDate:Long?
) {
    constructor():this("",null,null,null)
}