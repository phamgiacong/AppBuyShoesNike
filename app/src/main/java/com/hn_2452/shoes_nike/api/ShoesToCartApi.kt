package com.hn_2452.shoes_nike.api

import com.hn_2452.shoes_nike.data.Shoes
import com.hn_2452.shoes_nike.data.ShoesToCart
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShoesToCartApi {
    @GET("/shoesToCart/{idU}")
    suspend fun getShoesToCartByIdU(@Path("idU") idU:String) :List<ShoesToCart>
    @DELETE("/shoesToCart/{id}")
    suspend fun deleteShoesById(@Path("id")id:String):ShoesToCart
    @PUT("/shoesToCart/{id}")
    suspend fun updateShoesToCartById(@Path("id") id:String ,@Body shoesToCart: ShoesToCart):ShoesToCart
}