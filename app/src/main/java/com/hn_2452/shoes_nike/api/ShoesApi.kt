package com.hn_2452.shoes_nike.api

import com.hn_2452.shoes_nike.data.Shoes
import retrofit2.http.GET
import retrofit2.http.Path

interface ShoesApi {
    @GET("/shoes/{id}")
    suspend fun getShoesById(@Path("id")id:String):Shoes
}