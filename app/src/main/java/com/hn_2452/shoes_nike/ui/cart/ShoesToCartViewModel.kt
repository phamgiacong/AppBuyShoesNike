package com.hn_2452.shoes_nike.ui.cart

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.CartRepository
import com.hn_2452.shoes_nike.data.model.ShoesToCart
import com.hn_2452.shoes_nike.utility.Resource
import kotlinx.coroutines.Dispatchers

class ShoesToCartViewModel(app:Application):ViewModel() {
    private val cartRepository: CartRepository = CartRepository(app)

    fun getShoesToCartByIdU(idU:String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(cartRepository.getShoesToCartByIdU(idU)))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!!"))
        }
    }
    fun deleteShoesToCartById(id:String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(cartRepository.deleteShoesToCartById(id)))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!!"))
        }
    }
    fun updateShoesToCartById(id:String,shoesToCart: ShoesToCart) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(cartRepository.updateShoesToCartById(id, shoesToCart)))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!!"))
        }
    }
    class ShoesToCartViewModelFactory(private val app: Application): ViewModelProvider.Factory{
        override  fun <T:ViewModel> create(modelClass: Class<T>):T{
            if(modelClass.isAssignableFrom(ShoesToCartViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ShoesToCartViewModel(app) as T
            }else{
                throw  IllegalArgumentException("Unable contruct viewModel")
            }
        }
    }
}