package com.hn_2452.shoes_nike.repository

import android.app.Application
import com.hn_2452.shoes_nike.api.ApiConfig
import com.hn_2452.shoes_nike.data.Cart
import com.hn_2452.shoes_nike.data.ShoesToCart

class Repository(app:Application) {
    suspend fun getAllShipping() = ApiConfig.apiShippingService.getAllShipping()
    suspend fun getShippingById(id:String) = ApiConfig.apiShippingService.getShippingById(id)
    suspend fun getAllPromo()=ApiConfig.apiPromoService.getAllPromo()
    suspend fun getAddressByIdU(idU:String)=ApiConfig.apiAddressService.getAddressByIdU(idU)
    suspend fun getAddressByID(id:String) = ApiConfig.apiAddressService.getAddressById(id)
    suspend fun getShoesToCartByIdU(idU: String) = ApiConfig.apiShoesToCartService.getShoesToCartByIdU(idU)
    suspend fun getShoesById(id:String) = ApiConfig.apiShoesService.getShoesById(id)
    suspend fun deleteShoesToCartById(id:String) =ApiConfig.apiShoesToCartService.deleteShoesById(id)
    suspend fun updateShoesToCartById(id:String,shoesToCart: ShoesToCart) = ApiConfig.apiShoesToCartService.updateShoesToCartById(id,shoesToCart)
    suspend fun postCart(idU: String,cart: Cart) = ApiConfig.apiCartService.postCart(idU,cart)
    suspend fun getCart(idU: String)=ApiConfig.apiCartService.getCart(idU)
    suspend fun updateAddressToCart(id:String,cart: Cart) = ApiConfig.apiCartService.updateAddres(id,cart)
    suspend fun updateShippingToCart(id: String,cart: Cart)= ApiConfig.apiCartService.updateShipping(id,cart)
}