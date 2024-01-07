package com.hn_2452.shoes_nike.ui.home.shoes

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.local.NikeDatabase
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.data.repository.OrderDetailRepository
import com.hn_2452.shoes_nike.data.repository.ShoesRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ShoesViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val mShoesRepository: ShoesRepository,
    private val mAuthRepository: AuthRepository,
    private val mOrderDetailRepository: OrderDetailRepository,
    mNikeDatabase: NikeDatabase
) : ViewModel() {

    private val _mCurrentShoes = MutableLiveData<Shoes>()
    val mCurrentShoes: LiveData<Shoes> = _mCurrentShoes

    var mSelectedColor: String? = null
    var mSelectedSize: Int = -1
    var mSelectedNumber: Int = 1

    val mUsers = mNikeDatabase.userDao().getUsers()


    fun getShoesById(id: String) = liveData {
        try {
            emit(Resource.loading(null))
            emit(mShoesRepository.getShoesById(id))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "Error"))
        }
    }

    fun buyNow() = liveData {
        if (mCurrentShoes.value == null) {
            Toast.makeText(
                mContext,
                mContext.getString(R.string.application_not_ready),
                Toast.LENGTH_SHORT
            ).show()
            return@liveData
        }

        if (mSelectedColor.isNullOrEmpty()) {
            Toast.makeText(
                mContext,
                mContext.getString(R.string.request_select_color),
                Toast.LENGTH_SHORT
            ).show()
            return@liveData
        }

        if (mSelectedSize == -1) {
            Toast.makeText(
                mContext,
                mContext.getString(R.string.request_select_size),
                Toast.LENGTH_SHORT
            ).show()
            return@liveData
        }

        emit(
            OrderDetail(
                "",
                "",
                mCurrentShoes.value!!,
                mSelectedNumber,
                mSelectedColor!!,
                mSelectedSize
            )
        )
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

    fun addFavoriteShoes(id: String) = liveData {
        try {
            emit(Resource.loading())
            emit(mAuthRepository.addFavoriteShoes(id))
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

    fun checkFavoriteShoes(id: String) = liveData {
        try {
            emit(Resource.loading())
            emit(mAuthRepository.checkFavoriteShoes(id))
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

}
