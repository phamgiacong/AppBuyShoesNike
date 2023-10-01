package com.hn_2452.shoes_nike.ui.orders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hn_2452.shoes_nike.data.model.OrderModel

class BaseOrderViewModel : ViewModel() {
    var order = MutableLiveData<List<OrderModel>>()

    init {
        val data = arrayListOf(
            OrderModel
                .Builder()
                .nameProduct("Nike Tech Hera")
                .colorProduct("#FF0000")
                .sizeProduct("42")
                .quantity(1)
                .imageProduct("https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/4b2f5e8a-8638-4fa5-81fa-e714024efb39/tech-hera-shoes-JlV5km.png")
                .total(12300)
                .orderPlaced(true)
                .build(),

            OrderModel
                .Builder()
                .nameProduct("Nike Tech Hera")
                .colorProduct("#FF0000")
                .sizeProduct("42")
                .quantity(1)
                .imageProduct("https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/4b2f5e8a-8638-4fa5-81fa-e714024efb39/tech-hera-shoes-JlV5km.png")
                .total(12300)
                .orderShipped(true)
                .build(),

            OrderModel
                .Builder()
                .nameProduct("Nike Tech Hera")
                .colorProduct("#FF0000")
                .sizeProduct("42")
                .quantity(1)
                .imageProduct("https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/4b2f5e8a-8638-4fa5-81fa-e714024efb39/tech-hera-shoes-JlV5km.png")
                .total(12300)
                .orderShipped(true)
                .build(),

            OrderModel
                .Builder()
                .nameProduct("Nike Tech Hera")
                .colorProduct("#FF0000")
                .sizeProduct("42")
                .quantity(1)
                .imageProduct("https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/4b2f5e8a-8638-4fa5-81fa-e714024efb39/tech-hera-shoes-JlV5km.png")
                .total(12300)
                .orderShipped(true)
                .build(),

            OrderModel
                .Builder()
                .nameProduct("Nike Tech Hera")
                .colorProduct("#FF0000")
                .sizeProduct("42")
                .quantity(1)
                .imageProduct("https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/4b2f5e8a-8638-4fa5-81fa-e714024efb39/tech-hera-shoes-JlV5km.png")
                .total(12300)
                .orderDelivered(true)
                .build()
        )
        order.postValue(data)

    }
}