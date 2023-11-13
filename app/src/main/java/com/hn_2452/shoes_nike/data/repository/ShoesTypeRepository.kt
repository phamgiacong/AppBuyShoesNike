package com.hn_2452.shoes_nike.data.repository

import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.data.model.ShoesType
import com.hn_2452.shoes_nike.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShoesTypeRepository @Inject constructor() {
    suspend fun getShoesType(): Resource<List<ShoesType>> = withContext(Dispatchers.IO) {
        val networkResult = NikeService.mShoesTypeApi.getShoesType()
        if (networkResult.success) {
            return@withContext Resource.success(networkResult.data)
        } else {
            return@withContext Resource.error(null, networkResult.message)
        }
    }
}

