package com.hn_2452.shoes_nike.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.repository.Repository
import com.hn_2452.shoes_nike.ultils.Resoucce
import kotlinx.coroutines.Dispatchers

class AddressViewModel(app:Application): ViewModel() {
    private val  repository : Repository = Repository(app)
    fun getAddressByIdU(idU:String) = liveData(Dispatchers.IO){
        emit(Resoucce.loading(null))
        try {
            emit(Resoucce.success(repository.getAddressByIdU(idU)))
        }catch (ex:Exception){
            emit(Resoucce.error(null,ex.message?:"Error!!"))
        }
    }
    fun  getAddressById(id:String)= liveData(Dispatchers.IO){
        emit(Resoucce.loading(null))
        try {
            emit(Resoucce.success(repository.getAddressByID(id)))
        }catch (ex:Exception){
            emit(Resoucce.error(null,ex.message?:"Error!!"))
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