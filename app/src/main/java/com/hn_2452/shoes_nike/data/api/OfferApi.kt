package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.NetworkResult
import com.hn_2452.shoes_nike.data.model.Offer
import retrofit2.http.GET

interface OfferApi {
    @GET("/offer/get-available-offer")
    suspend fun getAvailableOffer(): NetworkResult<List<Offer>>
}