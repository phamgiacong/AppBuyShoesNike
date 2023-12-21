package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.NetworkResult
import com.hn_2452.shoes_nike.data.model.Order
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface OrderApi {

    @FormUrlEncoded
    @POST("/order")
    suspend fun createNewOrder(
        @Header("Authorization") token: String,
        @Field("address") address: String,
        @Field("offer") offer: String?,
        @Field("order_details[]") orderDetail: List<String>,
        @Field("payment_method") paymentMethod: Int,
        @Field("total_price") totalPrice: Long,
        @Field("sale") sale: Long
    ): NetworkResult<Nothing>

    @FormUrlEncoded
    @POST("/order/byRawData")
    suspend fun createNewOrderByRawData(
        @Header("Authorization") token: String,
        @Field("address") address: String,
        @Field("offer") offer: String?,
        @Field("payment_method") paymentMethod: Int,
        @Field("total_price") totalPrice: Long,
        @Field("sale") sale: Long,
        @Field("shoes_id") shoesId: String,
        @Field("quantity") quantity: Int,
        @Field("size") size: Int,
        @Field("color") color: String
    ): NetworkResult<Nothing>



    @GET("/order/getByUserId")
    suspend fun getOrderOfUser(
        @Header("Authorization") token: String,
        @Query("active") active: Boolean
    ): NetworkResult<List<Order>>

    @PUT("/order/cancelOrder")
    suspend fun cancelOrder(
        @Header("Authorization") token: String,
        @Field("id") id: String
    ): NetworkResult<Nothing>

}