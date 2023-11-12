package com.hn_2452.shoes_nike.data.repository

import com.hn_2452.shoes_nike.data.ShoesAPI
import com.hn_2452.shoes_nike.data.adapter.OfferAdapter
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.utility.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class OfferRepository(
    private val mNikeAPI: ShoesAPI,
    private val mOfferAdapter: OfferAdapter,
    override var mDispatcher: CoroutineDispatcher = Dispatchers.IO
): BaseRepository(mDispatcher) {
    suspend fun getAvailableOffer(): Result<List<Offer>> = run {
        mOfferAdapter.networkDataToOfferList(mNikeAPI.getAvailableOffer())
    }
}