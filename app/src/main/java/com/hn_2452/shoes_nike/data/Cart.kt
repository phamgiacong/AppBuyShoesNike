package com.hn_2452.shoes_nike.data


data class Cart(
    var _id:String?,
    var idU:String?,
    var idShoesToCart:ArrayList<String>?,
    var idPromo:String?,
    var idAddress :String?,
    var idShipping:String?,
    var createdDate:Long?
){
    constructor():this(null,null,null,null,null,null,null)
}
