package com.hn_2452.shoes_nike.data

import com.hn_2452.shoes_nike.BASE_URL
import com.hn_2452.shoes_nike.data.api.AddressApi
import com.hn_2452.shoes_nike.data.api.AuthApi
import com.hn_2452.shoes_nike.data.api.NotificationApi
import com.hn_2452.shoes_nike.data.api.OfferApi
import com.hn_2452.shoes_nike.data.api.OrderApi
import com.hn_2452.shoes_nike.data.api.OrderDetailApi
import com.hn_2452.shoes_nike.data.api.ShoesApi
import com.hn_2452.shoes_nike.data.api.ShoesTypeApi
import com.hn_2452.shoes_nike.data.api.TokenApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object NikeService {
    private val mRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .writeTimeout(30000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .build()
        )
        .build()

    val mAddressApi: AddressApi = mRetrofit.create(AddressApi::class.java)
    val mShoesApi: ShoesApi = mRetrofit.create(ShoesApi::class.java)
    val mShoesTypeApi: ShoesTypeApi = mRetrofit.create(ShoesTypeApi::class.java)
    val mOfferApi: OfferApi = mRetrofit.create(OfferApi::class.java)
    val mAuthApi: AuthApi = mRetrofit.create(AuthApi::class.java)
    val mOrderDetailApi: OrderDetailApi = mRetrofit.create(OrderDetailApi::class.java)
    val mOrderApi: OrderApi = mRetrofit.create(OrderApi::class.java)
    val mTokenApi :TokenApi = mRetrofit.create(TokenApi::class.java)
    val mNotificationApi :NotificationApi = mRetrofit.create(NotificationApi::class.java)
}



