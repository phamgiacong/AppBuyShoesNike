package com.hn_2452.shoes_nike.api

import com.hn_2452.shoes_nike.data.Cart
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartApi {
    @GET("/cart/{idU}")
    suspend fun getCart(@Path("idU") idU:String):List<Cart>
    @POST("/cart/{idU}")
    suspend fun postCart(@Path("idU") idU: String,@Body cart: Cart):Cart
    @PUT("/cart/updateAddress/{id}")
    suspend fun updateAddres(@Path("id") id:String,@Body cart: Cart)
    @PUT("/cart/updateShipping/{id}")
    suspend fun updateShipping(@Path("id") id:String,@Body cart: Cart)

}