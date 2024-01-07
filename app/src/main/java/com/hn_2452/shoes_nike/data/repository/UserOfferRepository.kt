package com.hn_2452.shoes_nike.data.repository

import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserOfferRepository @Inject constructor() {

    suspend fun createUserOffer(token: String, offerId: String) =
        withContext(Dispatchers.IO) {
            val res = NikeService.mUserOfferApi.createUserOffer(token, offerId)
            if (res.success) {
                return@withContext Resource.success(true)
            } else {
                return@withContext Resource.error(message = res.message)
            }
        }

    suspend fun getUserOfferByUserId(token: String) =
        withContext(Dispatchers.IO) {
            val res = NikeService.mUserOfferApi.getByUserId(token)
            if (res.success) {
                return@withContext Resource.success(res.data)
            } else {
                return@withContext Resource.error(message = res.message)
            }
        }

    suspend fun updateUserOffer(token: String, id: String) =
        withContext(Dispatchers.IO) {
            val res = NikeService.mUserOfferApi.updateUserOffer(token, id)
            if (res.success) {
                return@withContext Resource.success(true)
            } else {
                return@withContext Resource.error(message = res.message)
            }
        }
}