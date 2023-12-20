package com.hn_2452.shoes_nike.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.local.NikeDatabase
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.data.repository.OrderDetailRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val mOrderDetailRepository: OrderDetailRepository,
    private val mNikeDatabase: NikeDatabase
) : ViewModel() {

    var mCurrentUser = mNikeDatabase.userDao().getUsers()

    var mAvailableToCheckout: Boolean = false

    fun getCartOfUser() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(mOrderDetailRepository.getOrderDetailOfUser())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "Error!!!"))
        }
    }

    fun deleteCartItem(cartItemId: String) = liveData {
        try {
            emit(Resource.loading())
            emit(mOrderDetailRepository.deleteOrderDetail(cartItemId))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "Error!!!"))
        }
    }

    fun updateCartItem(cartItemId: String, updatedQuantity: Int) = liveData {
        try {
            emit(Resource.loading())
            emit(mOrderDetailRepository.updateOrderDetail(cartItemId, updatedQuantity))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "Error!!!"))
        }
    }

}