package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.NetworkResult
import com.hn_2452.shoes_nike.data.model.Shoes
import retrofit2.http.GET
import retrofit2.http.Path

interface ShoesApi {
    @GET("/shoes/{id}")
    suspend fun getShoesById(@Path("id") id: String): Shoes

    @GET("/shoes?sort_sold=1&state=1&limit=20")
    suspend fun getPopularShoes(): NetworkResult<List<Shoes>>
}