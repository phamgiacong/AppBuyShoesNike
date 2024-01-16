package com.hn_2452.shoes_nike.ui.cart.offer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.TOKEN_METHOD
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.data.repository.OfferRepository
import com.hn_2452.shoes_nike.data.repository.UserOfferRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import com.hn_2452.shoes_nike.utility.handleEx
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AppliedOfferViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val mUserOfferRepository: UserOfferRepository
) : ViewModel() {

    fun getOfferOfUser() = liveData(handleEx(mContext)) {
        try {
            emit(Resource.loading(null))
            emit(mUserOfferRepository.getUserOfferByUserId(
               TOKEN_METHOD + getStringDataByKey(mContext, TOKEN)
            ))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }
}