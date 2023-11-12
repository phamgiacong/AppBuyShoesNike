package com.hn_2452.shoes_nike.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.data.model.ShoesType
import com.hn_2452.shoes_nike.data.repository.OfferRepository
import com.hn_2452.shoes_nike.data.repository.ShoesRepository
import com.hn_2452.shoes_nike.data.repository.ShoesTypeRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mShoesTypeRepository: ShoesTypeRepository,
    private val mShoesRepository: ShoesRepository,
    private val mOfferRepository: OfferRepository
) : ViewModel() {

    companion object {
        private const val TAG = "Nike:HomeViewModel: "
    }

    private val _mShoesType = MutableLiveData<List<ShoesType>>()
    val mShoesType: LiveData<List<ShoesType>> = _mShoesType

    private val _mShoes = MutableLiveData<List<Shoes>>()
    val mShoes: LiveData<List<Shoes>> = _mShoes

    private val _mAvailableOffer = MutableLiveData<List<Offer>>()
    val mAvailableOffer : LiveData<List<Offer>> = _mAvailableOffer

    fun getShoesType() {
        Log.i(TAG, "getShoesType: ")
        viewModelScope.launch {
            val shoesTypeData = mShoesTypeRepository.getShoesType()
            if (shoesTypeData.success) {
                _mShoesType.value = shoesTypeData.data!!
            } else {
                shoesTypeData.exception?.printStackTrace()
            }
        }
    }

    fun getPopularShoes() {
        Log.i(TAG, "getPopularShoes: ")
        viewModelScope.launch {
            val shoesData = mShoesRepository.getPopularShoes()
            if (shoesData.success) {
                Log.i(TAG, "getPopularShoes: success; data=${shoesData.data}")
                _mShoes.value = shoesData.data!!
            } else {
                shoesData.exception?.printStackTrace()
            }
        }
    }

    fun getAvailableOffer() {
        Log.i(TAG, "getAvailableOffer: ")
        viewModelScope.launch {
            val offerData = mOfferRepository.getAvailableOffer()
            if(offerData.success) {
                _mAvailableOffer.value = offerData.data!!
            } else {
                offerData.exception?.printStackTrace()
            }
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