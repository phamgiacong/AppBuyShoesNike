package com.hn_2452.shoes_nike.data.repository

import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

class TokenRepository@Inject constructor() {
    suspend fun sendRegistrationTokenToServer(userId:String? ,token:String) = withContext(Dispatchers.IO){
        val response = NikeService.mTokenApi.sendRegistrationTokenToServer(userId,token)
        if (response.success) {
            Resource.success(true)
        } else {
            Resource.error(message = response.message)
        }
    }
}