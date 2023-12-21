package com.hn_2452.shoes_nike.data.repository

import android.content.Context
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

class OrderRepository @Inject constructor(
    @ApplicationContext private val mApp: Context,
    private val mNikeDatabase: NikeDatabase
) {

    suspend fun createNewOrder(
        address: String,
        offer: String,
        orderDetails: List<String>,
        paymentMethod: Int,
        totalPrice: Long,
        sale: Long
    ) : Resource<Boolean> = withContext(Dispatchers.IO) {
        val token = getStringDataByKey(mApp, TOKEN)
        if (token.isEmpty()) {
            return@withContext Resource.error(data = null, message = "Invalidate token")
        }

        val response = NikeService.mOrderApi.createNewOrder(
            TOKEN_METHOD + token,
            address,
            offer,
            orderDetails,
            paymentMethod,
            totalPrice,
            sale
        )
        if (response.success) {
            Resource.success(true)
        } else {
            Resource.error(message = response.message)
        }
    }

    suspend fun getOrderOfUser() = withContext(Dispatchers.IO) {
        val token = getStringDataByKey(mApp, TOKEN)
        if (token.isEmpty()) {
            return@withContext Resource.error(message = "Invalidate token")
        }

        val response = NikeService.mOrderApi.getOrderOfUser(TOKEN_METHOD + token)
        if (response.success) {
            Resource.success(response.data)
        } else {
            Resource.error(message = response.message)
        }
    }

    suspend fun cancelOrder(id: String) = withContext(Dispatchers.IO) {
        val token = getStringDataByKey(mApp, TOKEN)
        if (token.isEmpty()) {
            Resource.error(data = null, message = "Invalidate token")
            return@withContext
        }

        val response = NikeService.mOrderApi.cancelOrder(TOKEN_METHOD + token, id)
        if (response.success) {
            Resource.success(true)
        } else {
            Resource.error(message = response.message)
        }
    }


}