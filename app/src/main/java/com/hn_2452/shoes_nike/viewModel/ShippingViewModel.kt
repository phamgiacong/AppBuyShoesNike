package com.hn_2452.shoes_nike.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.repository.Repository
import com.hn_2452.shoes_nike.ultils.Resoucce
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.IllegalArgumentException

class ShippingViewModel(app :Application) : ViewModel(){
    private val shippingRepository : Repository = Repository(app)
    fun getShipping() = liveData(Dispatchers.IO){
        emit(Resoucce.loading(null))
        try {
            emit(Resoucce.success(shippingRepository.getAllShipping()))
        }catch (ex:Exception){
            emit(Resoucce.error(null,ex.message?:"Error !!"))
        }
    }
    fun getShippingById(id:String) = liveData(Dispatchers.IO){
        emit(Resoucce.loading(null))
        try {
            emit(Resoucce.success(shippingRepository.getShippingById(id)))
        }catch (ex:Exception){
            emit(Resoucce.error(null,ex.message?:"Error !!"))
        }
    }

    class ShippingViewModelFactory(private val app: Application):ViewModelProvider.Factory{
        override fun <T: ViewModel> create(modelClass: Class<T>):T{
            if(modelClass.isAssignableFrom(ShippingViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ShippingViewModel(app) as T
            }else{
                throw  IllegalArgumentException("Unable construct viewModel")
            }
        }
    }
}