package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.model.Receipt
import retrofit2.http.Body
import retrofit2.http.POST

interface ReceiptApi {
    @POST("/receipt")
    suspend fun createReceipt(@Body receipt: Receipt):Receipt

}