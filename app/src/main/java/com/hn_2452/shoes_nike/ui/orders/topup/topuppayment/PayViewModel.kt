package com.hn_2452.shoes_nike.ui.orders.topup.topuppayment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.PayModel

class PayViewModel : ViewModel() {

    var payData = MutableLiveData<List<PayModel>>()

    init {

        val list = listOf(
            PayModel(
                R.drawable.paypal, "PayPal"
            ),
            PayModel(
                R.drawable.google, "GooGle"
            ),
            PayModel(
                R.drawable.apple, "AppPle"
            ),
            PayModel(
                R.drawable.mastercard, "Mastercard"
            ),
        )

        payData.value = list
    }
}