package com.hn_2452.shoes_nike.data.repository

import android.content.Context
import android.util.Log
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.TOKEN_METHOD
import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.data.local.NikeDatabase
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderDetailRepository @Inject constructor(
    @ApplicationContext private val mApp: Context,
    private val mNikeDatabase: NikeDatabase
) {

    suspend fun addOrderDetail(shoesId: String, number: Int, color: String, size: Int) =
        withContext(Dispatchers.IO) {
            val token = getStringDataByKey(mApp, TOKEN)
            val response =
                NikeService.mOrderDetailApi.addOrderDetail(TOKEN_METHOD + token, shoesId, number, color, size)
            if (response.success) {
                Resource.success(true)
            } else {
                Resource.error(message = response.message)
            }
        }

    suspend fun getOrderDetailOfUser() = withContext(Dispatchers.IO) {
        val token = getStringDataByKey(mApp, TOKEN)
        Log.e("TAG", "getOrderDetailOfUser: $token ", )
        val response = NikeService.mOrderDetailApi.getOrderDetailOfUser(TOKEN_METHOD + token)

        if (response.success) {
            Resource.success(response.data)
        } else {
            Resource.error(message = response.message)
        }
    }

    suspend fun updateOrderDetail(id: String, quantity: Int) = withContext(Dispatchers.IO) {
        val token = getStringDataByKey(mApp, TOKEN)
        val response = NikeService.mOrderDetailApi.updateOrderDetail(TOKEN_METHOD + token, id, quantity)
        if (response.success) {
            Resource.success(true)
        } else {
            Resource.error(message = response.message)
        }
    }

    suspend fun deleteOrderDetail(id: String) = withContext(Dispatchers.IO) {
        val token = getStringDataByKey(mApp, TOKEN)
        val response = NikeService.mOrderDetailApi.deleteOrderDetail(TOKEN_METHOD + token, id)
        if (response.success) {
            Resource.success(true)
        } else {
            Resource.error(message = response.message)
        }
    }

}