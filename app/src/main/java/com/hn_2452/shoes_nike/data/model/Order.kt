package com.hn_2452.shoes_nike.data.model

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("_id")
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("order_details")
    val orderDetails: List<OrderDetail>,
    val address: Address,
    val offer: Offer?,
    @SerializedName("order_date")
    val orderDate: Long,
    @SerializedName("receive_date")
    val receiveDate: Long,
    val status: Int,
    @SerializedName("payment_method")
    val paymentMethod: Int,
    @SerializedName("payment_complete")
    val paymentComplete: Boolean,
    @SerializedName("cancel_reason")
    val cancelReason: String,
    @SerializedName("total_price")
    val totalPrice: Long,
    val sale: Long
)
