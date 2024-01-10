package com.hn_2452.shoes_nike.data.repository

import android.content.Context
import android.util.Log
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.TOKEN_METHOD
import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.data.local.NikeDatabase
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import com.hn_2452.shoes_nike.utility.saveStringDataByKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val mApp: Context,
    private val mNikeDatabase: NikeDatabase
) {

    companion object {
        private const val TAG = "AuthRepository"
        private const val AUTH_METHOD = "Bearer "
        private const val AUTH_KEY = "Authorization"
    }

    suspend fun login(email: String, password: String): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            val response = NikeService.mAuthApi.login(email, password)
            val result = response.success
            if (result) {
                saveStringDataByKey(mApp, TOKEN, response.data.token)
                mNikeDatabase.userDao().deleteUser()
                mNikeDatabase.userDao().addUser(response.data.user)
                Resource.success(true)
            } else {
                Resource.error(null, response.message)
            }
        }

    suspend fun loginWith(
        accountType: Int,
        accountId: String,
        displayName: String,
        avatar: String,
        email: String?
    ): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            val response =
                NikeService.mAuthApi.loginWith(accountType, accountId, displayName, avatar, email)
            Log.i(TAG, "loginWith: $response")
            val result = response.success
            if (result) {
                saveStringDataByKey(mApp, TOKEN, response.data.token)
                mNikeDatabase.userDao().deleteUser()
                mNikeDatabase.userDao().addUser(response.data.user)
                Resource.success(true)
            } else {
                Resource.error(null, response.message)
            }

        }

    suspend fun requestRegister(email: String): Resource<String> =
        withContext(Dispatchers.IO) {
            val response = NikeService.mAuthApi.requestRegister(email)
            if (response.success) {
                Resource.success(response.data)
            } else {
                Resource.error(null, response.message)
            }
        }

    suspend fun register(email: String, password: String) =
        withContext(Dispatchers.IO) {
            val response = NikeService.mAuthApi.register(email, password)
            if (response.success) {
                saveStringDataByKey(mApp, TOKEN, response.data.token)
                mNikeDatabase.userDao().deleteUser()
                mNikeDatabase.userDao().addUser(response.data.user)
                Resource.success(true)
            } else {
                Resource.error(null, response.message)
            }
        }

    suspend fun findPassword(email: String): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            val response = NikeService.mAuthApi.findPassword(email)
            if (response.success) {
                Resource.success(true)
            } else {
                Resource.error(null, response.message)
            }
        }

    suspend fun changePasswordByAuthCode(email: String, authCode: String, newPassword: String) =
        withContext(Dispatchers.IO) {
            val response =
                NikeService.mAuthApi.changePasswordByAuthCode(email, authCode, newPassword)
            if (response.success) {
                Resource.success(true)
            } else {
                Resource.error(null, response.message)
            }
        }

    suspend fun autoLogin(token: String) =
        withContext(Dispatchers.IO) {
            val response = NikeService.mAuthApi.autoLogin(AUTH_METHOD + token)
            if (response.success) {
                mNikeDatabase.userDao().deleteUser()
                mNikeDatabase.userDao().addUser(response.data)
                Resource.success(true)
            } else {
                Resource.error(message = response.message)
            }
        }

    suspend fun changePassword(token: String, currentPassword: String, newPassword: String) =
        withContext(Dispatchers.IO) {
            val response = NikeService.mAuthApi.changePassword(token, currentPassword, newPassword)
            if (response.success) {
                Resource.success(true)
            } else {
                Resource.error(message = response.message)
            }
        }

    suspend fun getFavoriteShoesOfUser() = withContext(Dispatchers.IO) {
        val token = getStringDataByKey(mApp, TOKEN)
        if (token.isEmpty()) {
            return@withContext Resource.error(data = null, message = "token is empty")
        }

        val response = NikeService.mAuthApi.getFavoriteOfUser(TOKEN_METHOD + token)
        if (response.success) {
            Resource.success(response.data)
        } else {
            Resource.error(message = response.message)
        }
    }

    suspend fun addFavoriteShoes(id: String) = withContext(Dispatchers.IO) {
        val token = TOKEN_METHOD + getStringDataByKey(mApp, TOKEN)
        val response = NikeService.mAuthApi.addFavoriteShoes(token, id)
        if (response.success) {
            Resource.success(response.data)
        } else {
            Resource.error(message = response.message)
        }
    }

    suspend fun checkFavoriteShoes(id: String) = withContext(Dispatchers.IO) {
        val token = TOKEN_METHOD + getStringDataByKey(mApp, TOKEN)
        val response = NikeService.mAuthApi.checkFavoriteOfUser(token, id)
        if (response.success) {
            Resource.success(response.data)
        } else {
            Resource.error(message = response.message)
        }
    }

    suspend fun getUserInfo() = withContext(Dispatchers.IO) {
        val token = TOKEN_METHOD + getStringDataByKey(mApp, TOKEN)
        val response = NikeService.mAuthApi.getUserInfo(token)
        if (response.success) {
            Resource.success(response.data)
        } else {
            Resource.error(message = response.message)
        }
    }

    suspend fun uploadUserInfo(
        image: String?,
        fullName: String?,
        birthDay: String?,
        gender: String?,
        phoneNumber: String?
    ) = withContext(Dispatchers.IO) {
        val token = TOKEN_METHOD + getStringDataByKey(mApp, TOKEN)
        val response = NikeService.mAuthApi.updateUserInfo(token, image, fullName, birthDay, gender, phoneNumber)
        if (response.success) {
            Resource.success(response.data)
        } else {
            Resource.error(message = response.message)
        }
    }


}