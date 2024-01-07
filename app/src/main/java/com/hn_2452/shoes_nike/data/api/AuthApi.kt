package com.hn_2452.shoes_nike.data.api

import com.hn_2452.shoes_nike.data.NetworkResult
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.data.model.User
import com.hn_2452.shoes_nike.data.network_result.LoginResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

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

    @GET("user/getUserInfo")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ) : NetworkResult<User>

    @Multipart
    @PUT("user/updateInfo")
    suspend fun updateUserInfo(
        @Header("Authorization") token: String,
        @Part("image") image: MultipartBody.Part,
        @Part("name") name: RequestBody?,
        @Part("full_name") fullName: RequestBody?,
        @Part("birthday") birthDay: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part("phone_number") phoneNumber: RequestBody?
     ) : NetworkResult<Boolean>


    @GET("user/getFavoriteShoes")
    suspend fun getFavoriteOfUser(
        @Header("Authorization") token: String
    ) : NetworkResult<List<Shoes>>

    @GET("user/checkFavoriteShoes")
    suspend fun checkFavoriteOfUser(
        @Header("Authorization") token: String,
        @Query("id") id: String
    ) : NetworkResult<Boolean>

    @FormUrlEncoded
    @POST("user/addFavoriteShoes")
    suspend fun addFavoriteShoes(
        @Header("Authorization") token: String,
        @Field("id") id: String
    ) : NetworkResult<Boolean>



}