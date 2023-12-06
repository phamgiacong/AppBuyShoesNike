package com.hn_2452.shoes_nike.data.repository

import android.app.Application
import coil.size.Dimension
import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.data.model.Cart
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.data.model.ShoesToCart
import com.hn_2452.shoes_nike.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository(app:Application) {
    suspend fun getAllShipping() = NikeService.mShippingApi.getAllShipping()
    suspend fun getShippingById(id:String) = NikeService.mShippingApi.getShippingById(id)
    suspend fun getAllPromo()= NikeService.mPromoApi.getAllPromo()
    suspend fun getAddressByIdU(idU:String)= NikeService.mAddressApi.getAddressByIdU(idU)
    suspend fun getAddressByID(id:String) = NikeService.mAddressApi.getAddressById(id)
    suspend fun getShoesToCartByIdU(idU: String) = NikeService.mShoesToCartApi.getShoesToCartByIdU(idU)
    suspend fun getShoesById(id:String): Resource<Shoes> = withContext(Dispatchers.IO) {
        val shoesData = NikeService.mShoesApi.getShoesById(id)
        if(shoesData.success) {
            return@withContext Resource.success(shoesData.data)
        } else {
            return@withContext Resource.error(null, shoesData.message)
        }
    }
    suspend fun deleteShoesToCartById(id:String) = NikeService.mShoesToCartApi.deleteShoesById(id)
    suspend fun updateShoesToCartById(id:String,shoesToCart: ShoesToCart) = NikeService.mShoesToCartApi.updateShoesToCartById(id,shoesToCart)
    suspend fun postCart(idU: String,cart: Cart) = NikeService.mCartApi.postCart(idU,cart)
    suspend fun getCart(idU: String)= NikeService.mCartApi.getCart(idU)
    suspend fun updateAddressToCart(id:String,cart: Cart) = NikeService.mCartApi.updateAddres(id,cart)
    suspend fun updateShippingToCart(id: String,cart: Cart)= NikeService.mCartApi.updateShipping(id,cart)
    suspend fun updateTotalPrice(id: String,cart: Cart)= NikeService.mCartApi.updateTotalPrice(id,cart)
    suspend fun deleteCart(id:String) = NikeService.mCartApi.deleteCart(id)
}