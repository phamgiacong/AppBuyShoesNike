package com.hn_2452.shoes_nike.data.repository

import android.content.Context
import android.util.Log
import com.hn_2452.shoes_nike.TOKEN_METHOD
import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    @ApplicationContext private val mApp: Context
){
    suspend fun getAllNotification() = withContext(Dispatchers.IO){
        val response = NikeService.mNotificationApi.getAllNotification()
        if(response.success){
            Resource.success(response.data)
        }else{
            Resource.error(message = response.message)
        }
    }
    suspend fun getNotificationOfUser(token: String) = withContext(Dispatchers.IO){
        val response = NikeService.mNotificationApi.getNotificationOfUser(token)
        if(response.success){
            Resource.success(response.data)
        }else{
            Resource.error(message = response.message)
        }
    }
    suspend fun getNotificationOffer() = withContext(Dispatchers.IO){
        val response = NikeService.mNotificationApi.getNotificationOffer()
        if(response.success){
            Resource.success(response.data)
        }else{
            Resource.error(message = response.message)
        }
    }
    suspend fun updateSeenNotification(id:String) = withContext(Dispatchers.IO){
        val response = NikeService.mNotificationApi.updateSeenNotification(id)
        if (response.success) {
            Resource.success(true)
        } else {
            Resource.error(message = response.message)
        }
    }
    suspend fun getQuantityNotification(id:String)= withContext(Dispatchers.IO){
        val response = NikeService.mNotificationApi.getQuantityNotification(id)
        if (response.success) {
            Resource.success(response.data)
        } else {
            Resource.error(message = response.message)
        }
    }


}