package com.hn_2452.shoes_nike.ui.cart

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.CartRepository
import com.hn_2452.shoes_nike.utility.Resource
import kotlinx.coroutines.Dispatchers

class ShoesViewModel(app:Application):ViewModel() {

    private val cartRepository: CartRepository = CartRepository(app)

    fun getShoesById(id:String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(cartRepository.getShoesById(id))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!!"))
        }
    }
    class ShoesViewModelFactory(private val app: Application): ViewModelProvider.Factory{
        override  fun <T:ViewModel> create(modelClass: Class<T>):T{
            if(modelClass.isAssignableFrom(ShoesViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ShoesViewModel(app) as T
            }else{
                throw  IllegalArgumentException("Unable contruct viewModel")
            }
        }
    }
}