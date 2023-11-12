package com.hn_2452.shoes_nike.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.Cart
import com.hn_2452.shoes_nike.repository.Repository
import com.hn_2452.shoes_nike.ultils.Resource
import kotlinx.coroutines.Dispatchers

class CartViewModel(app:Application):ViewModel() {
    private val repository: Repository = Repository(app)
    fun postCart(idU: String,cart: Cart) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.postCart(idU,cart)))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!!"))
        }

    }
    fun getCart(idU:String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getCart(idU)))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!!"))
        }
    }
    fun updateAddress(id:String,cart: Cart) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.updateAddressToCart(id, cart)))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!!"))
        }
    }
    fun updateShipping(id:String,cart: Cart) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.updateShippingToCart(id, cart)))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!!"))
        }
    }

    class CartViewModelFactory(private val app: Application): ViewModelProvider.Factory{
        override  fun <T:ViewModel> create(modelClass: Class<T>):T{
            if(modelClass.isAssignableFrom(CartViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return CartViewModel(app) as T
            }else{
                throw  IllegalArgumentException("Unable contruct viewModel")
            }
        }
    }

}