package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.model.Shipping
import retrofit2.http.GET
import retrofit2.http.Path

interface ShippingApi {
    @GET("/shipping")
    suspend fun getAllShipping():List<Shipping>
    @GET("/shipping/getById/{id}")
    suspend fun getShippingById(@Path("id") id:String): Shipping
}