package com.hn_2452.shoes_nike.data.repository

import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "Nike:ShoesRepository: "

class ShoesRepository @Inject constructor() {
    suspend fun getPopularShoes(): Resource<List<Shoes>> = withContext(Dispatchers.IO) {
        val popularShoesData = NikeService.mShoesApi.getPopularShoes()
        if (popularShoesData.success) {
            return@withContext Resource.success(popularShoesData.data)
        } else {
            return@withContext Resource.error(null, popularShoesData.message)
        }
    }

    suspend fun getShoesById(id: String): Resource<Shoes> = withContext(Dispatchers.IO) {
        val shoesData = NikeService.mShoesApi.getShoesById(id)
        if (shoesData.success) {
            return@withContext Resource.success(shoesData.data)
        } else {
            return@withContext Resource.error(null, shoesData.message)
        }
    }
}