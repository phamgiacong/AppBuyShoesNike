package com.hn_2452.shoes_nike.ui.cart.offer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.data.repository.OfferRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppliedOfferViewModel @Inject constructor(
    private val mAuthRepository: AuthRepository
) : ViewModel() {

    var mOfferListOfUser : List<Offer> = emptyList()

    fun getOfferOfUser() = liveData {
        try {
            emit(Resource.loading(null))
            emit(mAuthRepository.getOffersOfUser())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }
}