package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.model.ShoesType
import com.hn_2452.shoes_nike.data.NetworkResult
import retrofit2.http.GET

interface ShoesTypeApi {
    @GET("/shoes-type")
    suspend fun getShoesType(): NetworkResult<List<ShoesType>>

}