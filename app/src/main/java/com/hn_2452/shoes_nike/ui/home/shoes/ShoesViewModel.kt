package com.hn_2452.shoes_nike.ui.home.shoes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.data.repository.OrderDetailRepository
import com.hn_2452.shoes_nike.data.repository.ShoesRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShoesViewModel @Inject constructor(
    private val mShoesRepository: ShoesRepository,
    private val mAuthRepository: AuthRepository,
    private val mOrderDetailRepository: OrderDetailRepository
) : ViewModel() {

    private val _mCurrentShoes = MutableLiveData<Shoes>()
    val mCurrentShoes: LiveData<Shoes> = _mCurrentShoes

    var mSelectedColor: String? = null
    var mSelectedSize: Int = -1
    var mSelectedNumber: Int = 1


    fun getShoesById(id: String) = liveData {
        try {
            emit(Resource.loading(null))
            emit(mShoesRepository.getShoesById(id))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "Error"))
        }
    }

    fun addOrderDetail() = liveData {
        try {

            if (mCurrentShoes.value == null) {
                emit(Resource.error(message = "Ứng dụng chưa sẵn sàng"))
                return@liveData
            }

            if (mSelectedColor.isNullOrEmpty()) {
                emit(Resource.error(message = "Vui lòng chọn màu"))
                return@liveData
            }

            if (mSelectedSize == -1) {
                emit(Resource.error(message = "Vui lòng chọn kích cỡ giày"))
                return@liveData
            }

            emit(Resource.loading())
            emit(
                mOrderDetailRepository.addOrderDetail(
                    mCurrentShoes.value?._id!!,
                    mSelectedNumber,
                    mSelectedColor!!,
                    mSelectedSize
                )
            )
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

    fun setCurrentShoes(shoes: Shoes) {
        _mCurrentShoes.value = shoes
    }

}
