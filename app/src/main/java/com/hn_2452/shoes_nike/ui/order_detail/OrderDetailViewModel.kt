package com.hn_2452.shoes_nike.ui.order_detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.TOKEN_METHOD
import com.hn_2452.shoes_nike.data.repository.OrderDetailRepository
import com.hn_2452.shoes_nike.data.repository.OrderRepository
import com.hn_2452.shoes_nike.data.repository.ShoesReviewRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import com.hn_2452.shoes_nike.utility.handleEx
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val mOrderRepository: OrderRepository,
    private val mOrderDetailRepository: OrderDetailRepository,
    private val mShoesReviewRepository: ShoesReviewRepository
) : ViewModel() {

    companion object {
        const val TAG = "Nike:OrderDetailViewModel: "
    }

    fun getOrderDetailById(id: String) = liveData(handleEx(mContext)) {
        emit(Resource.loading())
        try {
            emit(mOrderRepository.getOrderById(id))
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }


    fun cancelOrder(id: String, reason: String) = liveData(handleEx(mContext)) {
        emit(Resource.loading())
        try {
            emit(mOrderRepository.cancelOrder(id, reason))
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

    fun reviewShoes(orderId: String, shoesId: String, star: Float, comment: String) = liveData(
        handleEx(mContext)
    ) {
        Log.i(TAG, "reviewShoes: orderId=$orderId shoesId=$shoesId star=$star comment=$comment")
        try {
            val token = TOKEN_METHOD + getStringDataByKey(mContext, TOKEN)
            emit(Resource.loading())
            emit(mShoesReviewRepository.addShoesReview(token, orderId, shoesId, star, comment))
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

}