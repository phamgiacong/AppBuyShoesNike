package com.hn_2452.shoes_nike.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiConfig {
    private const val BASE_URL = "http:192.168.0.104:3000/"
    private var builder = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
    var retrofit = builder.build()
    var apiShippingService:ShippingApi = retrofit.create(ShippingApi::class.java)
    var apiPromoService:PromoApi = retrofit.create(PromoApi::class.java)
    var apiAddressService:AddressApi = retrofit.create(AddressApi::class.java)
    var apiShoesToCartService :ShoesToCartApi = retrofit.create(ShoesToCartApi::class.java)
    var apiShoesService:ShoesApi = retrofit.create(ShoesApi::class.java)
    var apiCartService:CartApi = retrofit.create(CartApi::class.java)
}