package com.hn_2452.shoes_nike.data

import org.json.JSONObject
import retrofit2.http.GET

interface ShoesAPI {
    @GET("/shoes-type")
    suspend fun getShoesType() : JSONObject

    @GET("/shoes?sort_sold=1&state=1&limit=20")
    suspend fun getPopularShoes() : JSONObject

    @GET("/offer/get-available-offer")
    suspend fun getAvailableOffer(): JSONObject

}


