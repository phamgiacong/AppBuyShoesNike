package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.NetworkResult
import com.hn_2452.shoes_nike.data.model.UserOffer
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserOfferApi {

    @GET("user-offer/byUserId")
    suspend fun getByUserId(
        @Header("Authorization") token: String
    ): NetworkResult<List<UserOffer>>

    @FormUrlEncoded
    @POST("user-offer/")
    suspend fun createUserOffer(
        @Header("Authorization") token: String,
        @Field("offer_id") offerId: String
    ): NetworkResult<Nothing>

    @FormUrlEncoded
    @PUT("user-offer/")
    suspend fun updateUserOffer(
        @Header("Authorization") token: String,
        @Field("id") id: String,
        @Field("used") used: Boolean = true
    ): NetworkResult<Nothing>

}