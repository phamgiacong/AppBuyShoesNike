package com.hn_2452.shoes_nike.data.model


data class Cart(
    var _id:String?,
    var idU:String?,
    var idShoesToCart:ArrayList<String>?,
    var idAddress :String?,
    var idShipping:String?,
    var totalPrice:Double,
    var createdDate:Long?
){
    constructor():this(null,null,null,null,null,0.0,null)
}
