package com.hn_2452.shoes_nike.data.repository

import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfferRepository @Inject constructor() {
    suspend fun getAvailableOffer(): Resource<List<Offer>> = withContext(Dispatchers.IO) {
        val offerData = NikeService.mOfferApi.getAvailableOffer()
        if (offerData.success) {
            Resource.success(offerData.data)
        } else {
            Resource.error(null, offerData.message)
        }
    }

    suspend fun getOfferById(id: String): Resource<Offer> = withContext(Dispatchers.IO) {
        val offerData = NikeService.mOfferApi.getOfferById(id)
        if (offerData.success) {
            Resource.success(offerData.data)
        } else {
            Resource.error(null, offerData.message)
        }
    }
}