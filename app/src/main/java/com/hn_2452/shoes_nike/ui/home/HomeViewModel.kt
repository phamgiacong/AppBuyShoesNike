package com.hn_2452.shoes_nike.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
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
    private val mOfferRepository: OfferRepository
) : ViewModel() {
    companion object {
        private const val TAG = "Nike:HomeViewModel: "
    }

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

class HomeViewModelFactory(
    private val mShoesTypeRepository: ShoesTypeRepository,
    private val mShoesRepository: ShoesRepository,
    private val mOfferRepository: OfferRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(mShoesTypeRepository, mShoesRepository, mOfferRepository) as T
        }
        return super.create(modelClass)
    }
}