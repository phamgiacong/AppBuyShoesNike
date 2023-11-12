package com.hn_2452.shoes_nike.di

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.hn_2452.shoes_nike.BASE_URL
import com.hn_2452.shoes_nike.data.ShoesAPI
import com.hn_2452.shoes_nike.data.adapter.OfferAdapter
import com.hn_2452.shoes_nike.data.adapter.ShoesAdapter
import com.hn_2452.shoes_nike.data.adapter.ShoesTypeAdapter
import com.hn_2452.shoes_nike.data.repository.OfferRepository
import com.hn_2452.shoes_nike.data.repository.ShoesRepository
import com.hn_2452.shoes_nike.data.repository.ShoesTypeRepository
import com.hn_2452.shoes_nike.ui.home.HomeViewModel
import com.hn_2452.shoes_nike.ui.home.HomeViewModelFactory
import com.hn_2452.shoes_nike.utility.JSONConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


object LocalService {

    private val mNikeAPI: ShoesAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(JSONConverterFactory.create())
        .client(OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).build())
        .build()
        .create(ShoesAPI::class.java)

    private fun createShoesTypeAdapter(): ShoesTypeAdapter {
        return ShoesTypeAdapter()
    }

    private fun createShoesAdapter(): ShoesAdapter {
        return ShoesAdapter()
    }

    private fun createOfferAdapter(): OfferAdapter {
        return OfferAdapter()
    }

    private fun createShoesTypeRepository(): ShoesTypeRepository {
        return ShoesTypeRepository(
            mNikeAPI,
            createShoesTypeAdapter()
        )
    }

    private fun createShoesRepository(): ShoesRepository {
        return ShoesRepository(
            mNikeAPI,
            createShoesAdapter()
        )
    }

    private fun createOfferRepository() : OfferRepository {
        return OfferRepository(mNikeAPI, createOfferAdapter())
    }

    fun createHomeViewModel(owner: ViewModelStoreOwner) : HomeViewModel {
        return ViewModelProvider(
            owner,
            HomeViewModelFactory(
                createShoesTypeRepository(),
                createShoesRepository(),
                createOfferRepository()
            )
        )[HomeViewModel::class.java]
    }

}