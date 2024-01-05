package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.NetworkResult
import com.hn_2452.shoes_nike.data.model.ShoesReview
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ShoesReviewApi {

    @GET("/shoes-review/")
    suspend fun getShoesReview(
        @Query("shoes_id") shoesId: String
    ): NetworkResult<List<ShoesReview>>

    @FormUrlEncoded
    @POST("/shoes-review/")
    suspend fun addShoesReview(
        @Header("Authorization") token: String,
        @Field("order_id") orderId: String,
        @Field("shoes_id") shoesId: String,
        @Field("rate") rate: Float,
        @Field("comment") comment: String
    ) : NetworkResult<Boolean>



}