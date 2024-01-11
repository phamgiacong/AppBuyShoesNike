package com.hn_2452.shoes_nike.ui.orders

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.local.NikeDatabase
import com.hn_2452.shoes_nike.data.repository.OrderRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.handleEx
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val mOrderRepository: OrderRepository,
    mNikeDatabase: NikeDatabase,
    @ApplicationContext private val mContext: Context
) : ViewModel() {

    var mCurrentUser = mNikeDatabase.userDao().getUsers()

    fun getOrderOfUser(active: Boolean) = liveData(Dispatchers.IO + handleEx(mContext)) {
        emit(Resource.loading(null))
        try {
            emit(mOrderRepository.getOrderOfUser(active))
        } catch (ex: Exception) {
            emit(Resource.error(message =  ex.message!!))
        }
    }


}