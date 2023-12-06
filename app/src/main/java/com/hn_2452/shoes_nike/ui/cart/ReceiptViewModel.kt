package com.hn_2452.shoes_nike.ui.cart

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.model.Cart
import com.hn_2452.shoes_nike.data.model.Receipt
import com.hn_2452.shoes_nike.data.repository.ReceiptRespository
import com.hn_2452.shoes_nike.utility.Resource
import kotlinx.coroutines.Dispatchers

class ReceiptViewModel(application: Application): ViewModel() {
    private val receiptRespository:ReceiptRespository = ReceiptRespository(application)

    fun createReceipt(receipt: Receipt) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(receiptRespository.createReceipt(receipt)))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!!"))
        }

    }
    class ReceiptViewModelFactory(private val app: Application): ViewModelProvider.Factory{
        override  fun <T:ViewModel> create(modelClass: Class<T>):T{
            if(modelClass.isAssignableFrom(ReceiptViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ReceiptViewModel(app) as T
            }else{
                throw  IllegalArgumentException("Unable contruct viewModel")
            }
        }
    }
}