package com.hn_2452.shoes_nike.data.repository

import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.ui.searching.DEFAULT_SHOES_ID
import com.hn_2452.shoes_nike.ui.searching.GENDER_ALL
import com.hn_2452.shoes_nike.ui.searching.GENDER_MEN
import com.hn_2452.shoes_nike.ui.searching.SortAndFilter
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

    suspend fun getShoesByName(
        name: String,
        sortAndFilter: SortAndFilter
    ): Resource<List<Shoes>> = withContext(Dispatchers.IO) {
        val map = mutableMapOf<String, String>()
        map["name"] = name

        if (sortAndFilter.type.id != DEFAULT_SHOES_ID) {
            map["type"] = sortAndFilter.type.id
        }

        if (sortAndFilter.gender != GENDER_ALL) {
            map["gender"] = if (GENDER_MEN == sortAndFilter.gender) "1" else "2"
        }

        if(sortAndFilter.star != -1) {
            map["rate"] = sortAndFilter.star.toString()
        }

        map["min_price"] = sortAndFilter.priceRange.first.toString()
        map["max_price"] = sortAndFilter.priceRange.second.toString()
        map["sort"] = sortAndFilter.sort




        val shoesData = NikeService.mShoesApi.getShoesByName(map)
        if (shoesData.success) {
            return@withContext Resource.success(shoesData.data)
        } else {
            return@withContext Resource.error(null, shoesData.message)
        }
    }

    suspend fun getShoesByType(typeId: String): Resource<List<Shoes>> = withContext(Dispatchers.IO) {
        val shoesData = NikeService.mShoesApi.getShoesByType(typeId)
        if (shoesData.success) {
            return@withContext Resource.success(shoesData.data)
        } else {
            return@withContext Resource.error(null, shoesData.message)
        }
    }
}