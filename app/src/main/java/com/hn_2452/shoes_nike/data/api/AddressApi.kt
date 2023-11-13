package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.model.Address
import retrofit2.http.GET
import retrofit2.http.Path

interface AddressApi {
    @GET("/address/{idU}")
    suspend fun getAddressByIdU(@Path("idU") idU:String):List<Address>
    @GET("/address/getById/{id}")
    suspend fun getAddressById(@Path("id") id:String): Address
}