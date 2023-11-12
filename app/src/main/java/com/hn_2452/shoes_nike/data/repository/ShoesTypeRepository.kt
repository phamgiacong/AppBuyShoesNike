package com.hn_2452.shoes_nike.data.repository

import com.hn_2452.shoes_nike.data.ShoesAPI
import com.hn_2452.shoes_nike.data.adapter.ShoesTypeAdapter
import com.hn_2452.shoes_nike.data.model.ShoesType
import com.hn_2452.shoes_nike.utility.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ShoesTypeRepository(
    private val mShoesAPI: ShoesAPI,
    private val mShoesTypeAdapter: ShoesTypeAdapter,
    override val mDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository(mDispatcher) {
    suspend fun getShoesType(): Result<List<ShoesType>> = run {
        mShoesTypeAdapter.networkDataToShoesTypeList(mShoesAPI.getShoesType())
    }

}

