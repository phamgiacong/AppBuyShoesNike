package com.hn_2452.shoes_nike.ui.orders.topup.topupprice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hn_2452.shoes_nike.data.TopUpModel

class TopupViewModel : ViewModel() {

    var topupData = MutableLiveData<List<TopUpModel>>()

    init {

        val list = listOf(

            TopUpModel(
                "$10"
            ),
            TopUpModel(
                "$10"
            ),
            TopUpModel(
                "$10"
            ),
            TopUpModel(
                "$10"
            ),
            TopUpModel(
                "$10"
            ),
            TopUpModel(
                "$10"
            ),
            TopUpModel(
                "$10"
            ),
            TopUpModel(
                "$10"
            ),
            TopUpModel(
                "$10"
            )
        )

        topupData.value = list
    }
}