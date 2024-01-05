package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.NetworkResult
import com.hn_2452.shoes_nike.data.model.Notification
import retrofit2.http.GET

interface NotificationApi {
    @GET("/notification")
    suspend fun getAllNotification():NetworkResult<List<Notification>>
}