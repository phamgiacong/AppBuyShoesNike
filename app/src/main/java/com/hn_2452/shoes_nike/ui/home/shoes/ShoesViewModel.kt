package com.hn_2452.shoes_nike.ui.home.shoes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.data.repository.ShoesRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShoesViewModel @Inject constructor(
    private val mShoesRepository: ShoesRepository
) : ViewModel() {

    private val _mCurrentOrderDetail = MutableLiveData<OrderDetail>()
    val mCurrentOrderDetail: LiveData<OrderDetail> = _mCurrentOrderDetail

    private val _mCurrentShoes = MutableLiveData<Shoes>()
    val mCurrentShoes: LiveData<Shoes> = _mCurrentShoes


    fun getShoesById(id: String) = liveData {
        try {
            emit(Resource.loading(null))
            emit(mShoesRepository.getShoesById(id))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "Error"))
        }
    }

    fun setCurrentShoes(shoes: Shoes) {
        _mCurrentShoes.value = shoes
    }

    fun updateCurrentOrderDetail(orderDetail: OrderDetail?) {
        orderDetail?.let {
            _mCurrentOrderDetail.value = it
        }
    }

}

class ShoesViewModelFactory(
    private val mShoesRepository: ShoesRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoesViewModel::class.java)) {
            return ShoesViewModel(mShoesRepository) as T
        }
        throw IllegalArgumentException("unable to construct shoes view model")
    }
}