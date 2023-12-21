package com.hn_2452.shoes_nike.ui.cart.address

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.model.Address
import com.hn_2452.shoes_nike.data.repository.AddressRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val mAddressRepository: AddressRepository
) : ViewModel() {

    var mCurrentAddress: Address? = null

    companion object {
        const val TAG: String = "Nike:AddressViewModel: "
    }

    fun addNewAddress(
        type: Number = 0,
        address: String?,
        default: Boolean = false,
        userName: String?,
        phoneNumber: String?
    ) =
        liveData {
            try {

                if (address.isNullOrEmpty()) {
                    emit(Resource.error(message = "Vui lòng thêm địa chỉ"))
                    return@liveData
                }

                if (userName.isNullOrEmpty()) {
                    emit(Resource.error(message = "Vui lòng thêm tên người nhận"))
                    return@liveData
                }

                if (phoneNumber.isNullOrEmpty()) {
                    emit(Resource.error(message = "Vui lòng thêm số điện thoại người nhận"))
                    return@liveData
                }

                emit(Resource.loading())
                emit(
                    mAddressRepository.addNewAddress(
                        type,
                        address,
                        default,
                        userName,
                        phoneNumber
                    )
                )
            } catch (ex: Exception) {
                emit(Resource.error(message = ex.message!!))
            }
        }

    fun getAddressOfUser() = liveData {
        try {
            emit(Resource.loading())
            emit(mAddressRepository.getAddressOfUser())
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

    fun updateAddress(type: Number = 0, address: String?, default: Boolean = false, userName: String?, phoneNumber: String?) =
        liveData {
            try {

                val id = mCurrentAddress?.id
                if (id.isNullOrEmpty()) {
                    Log.i(TAG, "updateAddress: id is null")
                    emit(Resource.error(message = "Xảy ra lỗi"))
                    return@liveData
                }

                if (address.isNullOrEmpty()) {
                    emit(Resource.error(message = "Vui lòng thêm địa chỉ"))
                    return@liveData
                }

                if (userName.isNullOrEmpty()) {
                    emit(Resource.error(message = "Vui lòng thêm tên người nhận"))
                    return@liveData
                }

                if (phoneNumber.isNullOrEmpty()) {
                    emit(Resource.error(message = "Vui lòng thêm số điện thoại người nhận"))
                    return@liveData
                }

                emit(Resource.loading())
                emit(mAddressRepository.updateAddress(id, type, address, default, userName, phoneNumber))
            } catch (ex: Exception) {
                emit(Resource.error(message = ex.message!!))
            }
        }

    fun deleteAddress() = liveData {
        try {
            val id = mCurrentAddress?.id
            if (id.isNullOrEmpty()) {
                Log.i(TAG, "updateAddress: id is null")
                emit(Resource.error(message = "Xảy ra lỗi"))
                return@liveData
            }

            emit(Resource.loading())
            emit(mAddressRepository.deleteAddress(id))
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

}