package com.hn_2452.shoes_nike.data.repository

import android.util.Log
import com.hn_2452.shoes_nike.data.ShoesAPI
import com.hn_2452.shoes_nike.data.adapter.ShoesAdapter
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.utility.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

private const val TAG = "Nike:ShoesRepository: "
class ShoesRepository(
    private val mShoesAPI: ShoesAPI,
    private val mShoesAdapter: ShoesAdapter,
    override val mDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository(mDispatcher) {
    suspend fun getPopularShoes(): Result<List<Shoes>> = run {
        Log.i(TAG, "getPopularShoes: ")
        mShoesAdapter.networkDataToShoesList(mShoesAPI.getPopularShoes())
    }
}