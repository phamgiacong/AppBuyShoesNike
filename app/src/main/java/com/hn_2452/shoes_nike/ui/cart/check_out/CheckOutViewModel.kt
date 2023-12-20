package com.hn_2452.shoes_nike.ui.cart.check_out

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.model.Address
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.data.repository.AddressRepository
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.data.repository.OrderDetailRepository
import com.hn_2452.shoes_nike.data.repository.OrderRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class CheckOutViewModel @Inject constructor(
    private val mAddressRepository: AddressRepository,
    private val mOrderDetailRepository: OrderDetailRepository,
    private val mAuthRepository: AuthRepository,
    private val mOrderRepository: OrderRepository
) : ViewModel() {

    var mPrice: Long = -1
    var mTotalPrice = 0L

    var mCurrentAddress: MutableLiveData<Address?> = MutableLiveData()

    var mCurrentOffer: MutableLiveData<Offer?> = MutableLiveData()

    var mCurrentPaymentMethod = 0 // 0: cash, 1: online

    var mCurrentOrderDetailList: List<OrderDetail>? = null

    fun getDefaultAddress() = liveData {
        try {
            emit(Resource.loading())
            emit(mAddressRepository.getDefaultAddressOfUser())
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

    fun getCartOfUser() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(mOrderDetailRepository.getOrderDetailOfUser())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "Error!!!"))
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

    fun putNewOrder() = liveData {
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
                    mCurrentOffer.value?.id ?: "",
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

    fun deleteCartItem(cartItemId: String) = liveData {
        try {
            emit(Resource.loading())
            emit(mOrderDetailRepository.deleteOrderDetail(cartItemId))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "Error!!!"))
        }
    }




}