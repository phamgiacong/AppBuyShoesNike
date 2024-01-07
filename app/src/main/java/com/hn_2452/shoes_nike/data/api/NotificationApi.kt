package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.NetworkResult
import com.hn_2452.shoes_nike.data.model.Notification
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path

interface NotificationApi {
    @GET("/notification")
    suspend fun getAllNotification():NetworkResult<List<Notification>>

    @GET("/notification/get-by-id-user/{id_user}")
    suspend fun getNotificationOfUser(@Path("id_user") id_user:String):NetworkResult<List<Notification>>

    @GET("/notification/get-notification-offer")
    suspend fun getNotificationOffer():NetworkResult<List<Notification>>
}