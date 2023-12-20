package com.hn_2452.shoes_nike.data.repository

import android.content.Context
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.TOKEN_METHOD
import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddressRepository @Inject constructor(
    @ApplicationContext private val mApp: Context
) {

    suspend fun getAddressOfUser() = withContext(Dispatchers.IO) {
        val token = TOKEN_METHOD + getStringDataByKey(mApp, TOKEN)
        val response = NikeService.mAddressApi.getAddressByIdU(token)
        if (response.success) {
            Resource.success(response.data)
        } else {
            Resource.error(message = response.message)
        }
    }

    suspend fun addNewAddress(type: Number, address: String, default: Boolean, userName: String, phoneNumber: String) =
        withContext(Dispatchers.IO) {
            val token = TOKEN_METHOD + getStringDataByKey(mApp, TOKEN)
            val response = NikeService.mAddressApi.addNewAddress(token, type, address, default, userName, phoneNumber)
            if (response.success) {
                Resource.success(true)
            } else {
                Resource.error(message = response.message)
            }
        }

    suspend fun updateAddress(id: String, type: Number, address: String, default: Boolean, userName: String, phoneNumber: String) =
        withContext(Dispatchers.IO) {
            val token = TOKEN_METHOD + getStringDataByKey(mApp, TOKEN)
            val response = NikeService.mAddressApi.updateAddress(token, id, type, address, default, userName, phoneNumber)
            if (response.success) {
                Resource.success(true)
            } else {
                Resource.error(message = response.message)
            }
        }

    suspend fun deleteAddress(id: String) =
        withContext(Dispatchers.IO) {
            val token = TOKEN_METHOD + getStringDataByKey(mApp, TOKEN)
            val response = NikeService.mAddressApi.deleteAddress(token, id)
            if (response.success) {
                Resource.success(true)
            } else {
                Resource.error(message = response.message)
            }
        }

    suspend fun getDefaultAddressOfUser() = withContext(Dispatchers.IO) {
        val token = TOKEN_METHOD + getStringDataByKey(mApp, TOKEN)
        val response = NikeService.mAddressApi.getDefaultAddress(token)
        if (response.success) {
            Resource.success(response.data)
        } else {
            Resource.error(message = response.message)
        }
    }

}