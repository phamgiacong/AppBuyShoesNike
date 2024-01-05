package com.hn_2452.shoes_nike.ui.home.offer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.TOKEN_METHOD
import com.hn_2452.shoes_nike.data.repository.UserOfferRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class OfferViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val mUserOfferRepository: UserOfferRepository
) : ViewModel() {

    fun addOffer(offerId: String) = liveData {
        val token = TOKEN_METHOD + getStringDataByKey(mContext, TOKEN)
        try {
            emit(Resource.loading(null))
            emit(mUserOfferRepository.createUserOffer(token, offerId))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

    fun getOfferOfUser() = liveData {
        val token = TOKEN_METHOD + getStringDataByKey(mContext, TOKEN)
        try {
            emit(Resource.loading(null))
            emit(mUserOfferRepository.getUserOfferByUserId(token))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

}