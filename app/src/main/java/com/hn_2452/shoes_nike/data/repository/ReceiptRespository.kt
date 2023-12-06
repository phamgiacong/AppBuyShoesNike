package com.hn_2452.shoes_nike.data.repository

import android.app.Application
import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.data.api.ReceiptApi
import com.hn_2452.shoes_nike.data.model.Receipt

class ReceiptRespository(app:Application) {
    suspend fun createReceipt(receipt: Receipt) = NikeService.mReceiptApi.createReceipt(receipt)
}