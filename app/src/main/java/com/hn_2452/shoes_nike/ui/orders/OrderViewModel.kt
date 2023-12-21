package com.hn_2452.shoes_nike.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.OrderRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val mOrderRepository: OrderRepository
) : ViewModel() {

    fun getOrderOfUser() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(mOrderRepository.getOrderOfUser())
        } catch (ex: Exception) {
            emit(Resource.error(message =  ex.message!!))
        }
    }


}