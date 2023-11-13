package com.hn_2452.shoes_nike.ui.orders.e_wallet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.MyEWalletModel

class MyEWalletViewModel : ViewModel() {
    val eWalletData = MutableLiveData<List<MyEWalletModel>>()

    init {
        val list = arrayListOf(
            MyEWalletModel(
                R.drawable.bg_circle,
                R.drawable.shoe_svgrepo_com,
                "Air Jordan 3 Retro",
                "Dec 15, 2024",
                "10 Am",
                (105),
                "Top Up",
                R.drawable.arrow_up_upload_svgrepo_com
            ),

            MyEWalletModel(
                R.drawable.bg_circle,
                R.drawable.shoe_svgrepo_com,
                "Air Jordan 3 Retro",
                "Dec 15, 2024",
                "10 Am",
                (105),
                "Top Up",
                R.drawable.arrow_up_upload_svgrepo_com
            ),

            MyEWalletModel(
                R.drawable.bg_circle,
                R.drawable.shoe_svgrepo_com,
                "Air Jordan 3 Retro",
                "Dec 15, 2024",
                "10 Am",
                (105),
                "Top Up",
                R.drawable.arrow_up_upload_svgrepo_com
            ),

            MyEWalletModel(
                R.drawable.bg_circle,
                R.drawable.shoe_svgrepo_com,
                "Air Jordan 3 Retro",
                "Dec 15, 2024",
                "10 Am",
                (105),
                "Top Up",
                R.drawable.arrow_up_upload_svgrepo_com
            ),
        )
        eWalletData.value = list
    }
}