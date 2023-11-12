package com.hn_2452.shoes_nike.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.repository.Repository
import com.hn_2452.shoes_nike.data.ShoesToCart
import com.hn_2452.shoes_nike.ultils.Resource
import kotlinx.coroutines.Dispatchers

class ShoesToCartViewModel(app:Application):ViewModel() {
    private val repository: Repository = Repository(app)

    fun getShoesToCartByIdU(idU:String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getShoesToCartByIdU(idU)))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!!"))
        }
    }
    fun deleteShoesToCartById(id:String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.deleteShoesToCartById(id)))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!!"))
        }
    }
    fun updateShoesToCartById(id:String,shoesToCart: ShoesToCart) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.updateShoesToCartById(id, shoesToCart)))
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