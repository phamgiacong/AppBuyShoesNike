package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.NetworkResult
import com.hn_2452.shoes_nike.data.model.OrderDetail
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface OrderDetailApi {

    @FormUrlEncoded
    @POST("/order-detail/")
    suspend fun addOrderDetail(
        @Header("Authorization") token: String,
        @Field("shoes_id") shoesId: String,
        @Field("quantity") number: Int,
        @Field("color") color: String,
        @Field("size") size: Int,
    ): NetworkResult<Nothing>

    @FormUrlEncoded
    @PUT("/order-detail/")
    suspend fun updateOrderDetail(
        @Header("Authorization") token: String,
        @Field("id") orderDetailId: String,
        @Field("quantity") number: Int
    ): NetworkResult<Nothing>

    @DELETE("/order-detail/")
    suspend fun deleteOrderDetail(
        @Header("Authorization") token: String,
        @Query("id") orderDetailId: String,
    ): NetworkResult<Nothing>


    @GET("/order-detail/get-by-user-id")
    suspend fun getOrderDetailOfUser(
        @Header("Authorization") token: String,
    ): NetworkResult<List<OrderDetail>>

}