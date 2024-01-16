package com.hn_2452.shoes_nike.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.local.NikeDatabase
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.data.model.ShoesType
import com.hn_2452.shoes_nike.data.repository.OfferRepository
import com.hn_2452.shoes_nike.data.repository.ShoesRepository
import com.hn_2452.shoes_nike.data.repository.ShoesTypeRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.handleEx
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.handleCoroutineException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mShoesTypeRepository: ShoesTypeRepository,
    private val mShoesRepository: ShoesRepository,
    private val mOfferRepository: OfferRepository,
    mNikeDatabase: NikeDatabase,
    @ApplicationContext private val mContext: Context
) : ViewModel() {
    companion object {
        private const val TAG = "Nike:HomeViewModel: "
    }

    var mShoesTypeList : List<ShoesType>? = null
    var mOfferList : List<Offer>? = null
    var mShoesList: List<Shoes>? = null


    val mUsers = mNikeDatabase.userDao().getUsers()

    fun getShoesType() = liveData(handleEx(mContext)) {
        try {
            emit(Resource.loading(null))
            val res = mShoesTypeRepository.getShoesType()
            mShoesTypeList = res.data
            emit(res)
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

    fun getPopularShoes() = liveData(handleEx(mContext)) {
        try {
            emit(Resource.loading(null))
            val res = mShoesRepository.getPopularShoes()
            mShoesList = res.data
            emit(mShoesRepository.getPopularShoes())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

    fun getAvailableOffer() = liveData(handleEx(mContext)) {
        try {
            emit(Resource.loading(null))
            val res = mOfferRepository.getAvailableOffer()
            mOfferList = res.data
            emit(res)
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

    fun getOfferById(id: String) = liveData(handleEx(mContext)) {
        try {
            emit(Resource.loading(null))
            val res = mOfferRepository.getOfferById(id)
            emit(res)
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

}