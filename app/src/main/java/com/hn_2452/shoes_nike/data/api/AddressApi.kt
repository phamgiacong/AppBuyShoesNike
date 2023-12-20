package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.NetworkResult
import com.hn_2452.shoes_nike.data.model.Address
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AddressApi {
    @GET("/address/getByUserID")
    suspend fun getAddressByIdU(
        @Header("Authorization") token: String
    ): NetworkResult<List<Address>>

    @FormUrlEncoded
    @POST("/address")
    suspend fun addNewAddress(
        @Header("Authorization") token: String,
        @Field("type") type: Number,
        @Field("address") address: String,
        @Field("default") default: Boolean,
        @Field("user_name") userName: String,
        @Field("phone_number") phoneNumber: String
    ): NetworkResult<Nothing>

    @FormUrlEncoded
    @PUT("/address")
    suspend fun updateAddress(
        @Header("Authorization") token: String,
        @Field("id") id: String,
        @Field("type") type: Number,
        @Field("address") address: String,
        @Field("default") default: Boolean,
        @Field("user_name") userName: String,
        @Field("phone_number") phoneNumber: String
    ): NetworkResult<Nothing>

    @DELETE("/address/{id}")
    suspend fun deleteAddress(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): NetworkResult<Nothing>

    @GET("/address/geDefaultAddressByUserID")
    suspend fun getDefaultAddress(
        @Header("Authorization") token: String
    ) : NetworkResult<Address>


}