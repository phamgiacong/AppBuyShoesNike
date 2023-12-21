package com.hn_2452.shoes_nike.ui.home.offer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OfferViewModel @Inject constructor(
    private val mAuthRepository: AuthRepository
): ViewModel() {

    fun addOffer(offerId: String) = liveData {
        try {
            emit(Resource.loading(null))
            emit(mAuthRepository.addOffer(offerId))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

    fun getOfferOfUser() = liveData {
        try {
            emit(Resource.loading(null))
            emit(mAuthRepository.getOffersOfUser())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

}