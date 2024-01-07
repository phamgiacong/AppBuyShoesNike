package com.hn_2452.shoes_nike.data.repository

import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.utility.parseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ShoesReviewRepository @Inject constructor() {

    private var mDispatcher = Dispatchers.IO

    suspend fun getShoesReview(shoesId: String) = withContext(mDispatcher) {
        val res = NikeService.mShoesReview.getShoesReview(shoesId)
        return@withContext parseResult(res)
    }

    suspend fun addShoesReview(
        token: String,
        orderId: String,
        shoesId: String,
        rate: Float,
        comment: String
    ) = withContext(mDispatcher) {
        val res = NikeService.mShoesReview.addShoesReview(token, orderId, shoesId, rate, comment)
        return@withContext parseResult(res)
    }

}