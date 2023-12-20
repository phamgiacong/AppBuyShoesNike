package com.hn_2452.shoes_nike.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.local.NikeDatabase
import com.hn_2452.shoes_nike.data.repository.OfferRepository
import com.hn_2452.shoes_nike.data.repository.ShoesRepository
import com.hn_2452.shoes_nike.data.repository.ShoesTypeRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mShoesTypeRepository: ShoesTypeRepository,
    private val mShoesRepository: ShoesRepository,
    private val mOfferRepository: OfferRepository,
    mNikeDatabase: NikeDatabase
) : ViewModel() {
    companion object {
        private const val TAG = "Nike:HomeViewModel: "
    }

    val mUsers = mNikeDatabase.userDao().getUsers()

    fun getShoesType() = liveData {
        try {
            emit(Resource.loading(null))
            emit(mShoesTypeRepository.getShoesType())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

    fun getPopularShoes() = liveData {
        try {
            emit(Resource.loading(null))
            emit(mShoesRepository.getPopularShoes())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

    fun getAvailableOffer() = liveData {
        try {
            emit(Resource.loading(null))
            emit(mOfferRepository.getAvailableOffer())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

}