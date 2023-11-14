package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.model.Promo
import retrofit2.http.GET

interface PromoApi {
    @GET("/promo")
    suspend fun getAllPromo():List<Promo>

}