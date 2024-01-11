package com.hn_2452.shoes_nike.ui.cart.check_out

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.TOKEN_METHOD
import com.hn_2452.shoes_nike.data.model.Address
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.data.model.UserOffer
import com.hn_2452.shoes_nike.data.repository.AddressRepository
import com.hn_2452.shoes_nike.data.repository.OrderDetailRepository
import com.hn_2452.shoes_nike.data.repository.OrderRepository
import com.hn_2452.shoes_nike.data.repository.UserOfferRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import com.hn_2452.shoes_nike.utility.handleEx
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class CheckOutViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val mAddressRepository: AddressRepository,
    private val mOrderDetailRepository: OrderDetailRepository,
    private val mOrderRepository: OrderRepository,
    private val mUserOfferRepository: UserOfferRepository
) : ViewModel() {

    var mPrice: Long = -1
    var mTotalPrice = 0L

    var mCurrentAddress: MutableLiveData<Address?> = MutableLiveData()

    var mCurrentOffer: MutableLiveData<UserOffer?> = MutableLiveData()

    var mCurrentPaymentMethod = 0 // 0: cash, 1: online

    var mCurrentOrderDetailList: List<OrderDetail>? = null

    var mCurrentOrderDetail: OrderDetail? = null

    fun getDefaultAddress() = liveData(handleEx(mContext)) {
        try {
            emit(Resource.loading())
            emit(mAddressRepository.getDefaultAddressOfUser())
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

    fun getCartItemOfUser(cartItemIdList: List<String>) = liveData(Dispatchers.IO + handleEx(mContext)) {
        emit(Resource.loading(null))
        try {
            emit(mOrderDetailRepository.getOrderDetailOfUser(cartItemIdList))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "Error!!!"))
        }
    }

    fun getOfferOfUser() = liveData(handleEx(mContext)) {
        try {
            emit(Resource.loading(null))
            emit(mUserOfferRepository.getUserOfferByUserId(
                TOKEN_METHOD + getStringDataByKey(mContext, TOKEN))
            )
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

    fun putNewOrderByRawData() = liveData(handleEx(mContext)) {
        try {
            if (mCurrentOrderDetail == null) {
                emit(Resource.error(data = null, message = "Không có đơn hàng"))
                return@liveData
            }

            if (mCurrentAddress.value == null) {
                emit(Resource.error(data = null, message = "Cần thêm địa chỉ đặt hàng"))
                return@liveData
            }

            emit(Resource.loading(null))
            emit(
                mOrderRepository.createNewOrderByRawData(
                    mCurrentAddress.value?.id ?: "",
                    mCurrentOffer.value?.id,
                    0,
                    mTotalPrice,
                    mPrice - mTotalPrice,
                    mCurrentOrderDetail?.shoes?._id!!,
                    mCurrentOrderDetail?.quantity!!,
                    mCurrentOrderDetail?.size!!,
                    mCurrentOrderDetail?.color!!
                )
            )
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }


    fun putNewOrder() = liveData(handleEx(mContext)) {
        try {
            if (mCurrentOrderDetailList.isNullOrEmpty()) {
                emit(Resource.error(data = null, message = "Không có đơn hàng"))
                return@liveData
            }

            if (mCurrentAddress.value == null) {
                emit(Resource.error(data = null, message = "Cần thêm địa chỉ đặt hàng"))
                return@liveData
            }

            val orderDetailList = mutableListOf<String>()
            mCurrentOrderDetailList?.forEach {
                orderDetailList.add(it.id)
            }

            emit(Resource.loading(null))
            emit(
                mOrderRepository.createNewOrder(
                    mCurrentAddress.value?.id ?: "",
                    mCurrentOffer.value?.id,
                    orderDetailList,
                    0,
                    mTotalPrice,
                    mPrice - mTotalPrice
                )
            )
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

    fun clearData() {
        mPrice = -1
        mTotalPrice = 0L
        mCurrentAddress.value = null
        mCurrentOffer.value = null
        mCurrentPaymentMethod = 0 // 0: cash, 1: online
        mCurrentOrderDetailList = null
    }


}