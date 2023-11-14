package com.hn_2452.shoes_nike.data

import com.hn_2452.shoes_nike.BASE_URL
import com.hn_2452.shoes_nike.data.api.AddressApi
import com.hn_2452.shoes_nike.data.api.CartApi
import com.hn_2452.shoes_nike.data.api.OfferApi
import com.hn_2452.shoes_nike.data.api.PromoApi
import com.hn_2452.shoes_nike.data.api.ShippingApi
import com.hn_2452.shoes_nike.data.api.ShoesApi
import com.hn_2452.shoes_nike.data.api.ShoesToCartApi
import com.hn_2452.shoes_nike.data.api.ShoesTypeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NikeService {
    private val mRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val mShippingApi: ShippingApi = mRetrofit.create(ShippingApi::class.java)
    val mPromoApi: PromoApi = mRetrofit.create(PromoApi::class.java)
    val mAddressApi: AddressApi = mRetrofit.create(AddressApi::class.java)
    val mShoesToCartApi: ShoesToCartApi = mRetrofit.create(ShoesToCartApi::class.java)
    val mShoesApi: ShoesApi = mRetrofit.create(ShoesApi::class.java)
    val mCartApi: CartApi = mRetrofit.create(CartApi::class.java)
    val mShoesTypeApi: ShoesTypeApi = mRetrofit.create(ShoesTypeApi::class.java)
    val mOfferApi: OfferApi = mRetrofit.create(OfferApi::class.java)
}



