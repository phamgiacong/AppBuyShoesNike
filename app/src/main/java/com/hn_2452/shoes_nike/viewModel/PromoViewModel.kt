package com.hn_2452.shoes_nike.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.repository.Repository
import com.hn_2452.shoes_nike.ultils.Resource
import kotlinx.coroutines.Dispatchers

class PromoViewModel(app:Application):ViewModel() {
    private val repository : Repository = Repository(app)
    fun getPromo()= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getAllPromo()))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!"))
        }
    }
    class PromoViewModelFactory(private val app: Application):ViewModelProvider.Factory{
        override  fun <T:ViewModel> create(modelClass: Class<T>):T{
            if(modelClass.isAssignableFrom(PromoViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return PromoViewModel(app) as T
            }else{
                throw  IllegalArgumentException("Unable contruct viewModel")
            }
        }
    }
}