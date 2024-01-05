package com.example.zalopaykotlin.Api

import com.example.zalopaykotlin.Constant.AppInfo
import com.example.zalopaykotlin.Helper.Helpers
import okhttp3.FormBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.Date

class CreateOrder {
    private class CreateOrderData constructor(amount: String) {
        var AppId: String
        var AppUser: String
        var AppTime: String
        var Amount: String
        var AppTransId: String
        var EmbedData: String
        var Items: String
        var BankCode: String
        var Description: String
        var Mac: String

        init {
            val appTime = Date().time
            AppId = java.lang.String.valueOf(AppInfo.APP_ID)
            AppUser = "Android_Demo"
            AppTime = appTime.toString()
            Amount = amount
            AppTransId = Helpers.getAppTransId()
            EmbedData = "{}"
            Items = "[]"
            BankCode = "zalopayapp"
            Description = "Merchant pay for order #" + Helpers.getAppTransId()
            val inputHMac = String.format(
                "%s|%s|%s|%s|%s|%s|%s",
                AppId,
                AppTransId,
                AppUser,
                Amount,
                AppTime,
                EmbedData,
                Items
            )
            Mac = Helpers.getMac(AppInfo.MAC_KEY, inputHMac)
        }
    }

    @Throws(Exception::class)
    fun createOrder(amount: String): JSONObject? {
        val input = CreateOrderData(amount)
        val formBody: RequestBody = FormBody.Builder()
            .add("appid", input.AppId)
            .add("appuser", input.AppUser)
            .add("apptime", input.AppTime)
            .add("amount", input.Amount)
            .add("apptransid", input.AppTransId)
            .add("embeddata", input.EmbedData)
            .add("item", input.Items)
            .add("bankcode", input.BankCode)
            .add("description", input.Description)
            .add("mac", input.Mac)
            .build()
        return HttpProvider.sendPost(AppInfo.URL_CREATE_ORDER, formBody)
    }
}