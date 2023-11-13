package com.hn_2452.shoes_nike.ui.cart

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.CartRepository
import com.hn_2452.shoes_nike.utility.Resource
import kotlinx.coroutines.Dispatchers

class AddressViewModel(app:Application): ViewModel() {
    private val  cartRepository : CartRepository = CartRepository(app)
    fun getAddressByIdU(idU:String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(cartRepository.getAddressByIdU(idU)))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!"))
        }
    }
    fun  getAddressById(id:String)= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(cartRepository.getAddressByID(id)))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!"))
        }
    }
    class AddressViewModelFactory(private val app: Application): ViewModelProvider.Factory{
        override  fun <T:ViewModel> create(modelClass: Class<T>):T{
            if(modelClass.isAssignableFrom(AddressViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return AddressViewModel(app) as T
            }else{
                throw  IllegalArgumentException("Unable contruct viewModel")
            }
        }
    }
}