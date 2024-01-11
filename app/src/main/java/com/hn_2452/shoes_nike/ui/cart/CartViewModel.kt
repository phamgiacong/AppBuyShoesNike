package com.hn_2452.shoes_nike.ui.cart

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.local.NikeDatabase
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.data.repository.OrderDetailRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.handleEx
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val mOrderDetailRepository: OrderDetailRepository,
    private val mNikeDatabase: NikeDatabase,
    @ApplicationContext private val mContext: Context
) : ViewModel() {

    var mCurrentUser = mNikeDatabase.userDao().getUsers()

    var mSelectedCartItemIdList : MutableList<String>? = null
    var mCartItemList: MutableList<OrderDetail> = mutableListOf()

    fun getCartOfUser() = liveData(Dispatchers.IO + handleEx(mContext)) {
        emit(Resource.loading(null))
        try {
            emit(mOrderDetailRepository.getOrderDetailOfUser(emptyList()))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "Error!!!"))
        }
    }

    fun deleteCartItem(cartItemId: String) = liveData(handleEx(mContext)) {
        try {
            emit(Resource.loading())
            emit(mOrderDetailRepository.deleteOrderDetail(cartItemId))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "Error!!!"))
        }
    }

    fun updateCartItem(cartItemId: String, updatedQuantity: Int) = liveData(handleEx(mContext)) {
        try {
            emit(Resource.loading())
            emit(mOrderDetailRepository.updateOrderDetail(cartItemId, updatedQuantity))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "Error!!!"))
        }
    }

}