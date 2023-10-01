package com.hn_2452.shoes_nike

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hn_2452.shoes_nike.data.model.OrderModel

class MainViewModel: ViewModel() {
    var sendOrder =  MutableLiveData<OrderModel>()
}