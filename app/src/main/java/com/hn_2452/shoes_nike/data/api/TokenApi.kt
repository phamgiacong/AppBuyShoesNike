package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.NetworkResult
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TokenApi {
    @FormUrlEncoded
    @POST("/token")
    suspend fun sendRegistrationTokenToServer(
        @Field("userId")userId:String?,
        @Field("token") token:String
    ):NetworkResult<Nothing>
}