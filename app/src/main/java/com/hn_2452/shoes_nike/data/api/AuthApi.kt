package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.NetworkResult
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.data.network_result.LoginResult
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): NetworkResult<LoginResult>

    @FormUrlEncoded
    @POST("user/login-with-social-account")
    suspend fun loginWith(
        @Field("account_type") accountType: Int,
        @Field("account_id") accountId: String,
        @Field("display_name") displayName: String,
        @Field("avatar") avatar: String,
        @Field("email") email: String?
    ): NetworkResult<LoginResult>

    @FormUrlEncoded
    @POST("user/request_register")
    suspend fun requestRegister(
        @Field("email") email: String
    ): NetworkResult<String>


    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String
    ): NetworkResult<LoginResult>


    @FormUrlEncoded
    @POST("user/forget-password")
    suspend fun findPassword(
        @Field("email") email: String
    ): NetworkResult<Nothing>

    @POST("user/auto-login")
    suspend fun autoLogin(
        @Header("Authorization") token: String
    ): NetworkResult<Nothing>

    @FormUrlEncoded
    @POST("user/change-pass-by-code")
    suspend fun changePasswordByAuthCode(
        @Field("email") email: String,
        @Field("auth_code") authCode: String,
        @Field("new_password") newPassword: String
    ): NetworkResult<Nothing>

    @FormUrlEncoded
    @POST("user/change-password")
    suspend fun changePassword(
        @Field("current_password") currentPassword: String,
        @Field("new_password") newPassword: String
    ): NetworkResult<Nothing>


    @GET("user/offers")
    suspend fun getOfferOfUser(
        @Header("Authorization") token: String
    ) : NetworkResult<List<Offer>>

    @FormUrlEncoded
    @POST("user/offer")
    suspend fun addOffer(
        @Header("Authorization") token: String,
        @Field("id") id: String
    ) : NetworkResult<Nothing>

}