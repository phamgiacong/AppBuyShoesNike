package com.hn_2452.shoes_nike.data.repository

import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationRepository {
    suspend fun getAllNotification() = withContext(Dispatchers.IO){
        val response = NikeService.mNotificationApi.getAllNotification()
        if(response.success){
            Resource.success(response.data)
        }else{
            Resource.error(message = response.message)
        }
    }
}