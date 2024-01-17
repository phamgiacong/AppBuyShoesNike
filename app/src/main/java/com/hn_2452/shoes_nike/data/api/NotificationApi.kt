package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.NetworkResult
import com.hn_2452.shoes_nike.data.model.Notification
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotificationApi {
    @GET("/notification")
    suspend fun getAllNotification(): NetworkResult<List<Notification>>

    @GET("/notification/get-by-id-user/")
    suspend fun getNotificationOfUser(@Header("Authorization") token: String): NetworkResult<List<Notification>>

    @GET("/notification/get-notification-offer")
    suspend fun getNotificationOffer(): NetworkResult<List<Notification>>

    @PUT("/notification/{idN}")
    suspend fun updateSeenNotification(@Path("idN") id: String): NetworkResult<Nothing>

    @GET("/notification/get-quantity-notification/{idU}")
    suspend fun getQuantityNotification(@Path("idU") id: String): NetworkResult<Int>
}