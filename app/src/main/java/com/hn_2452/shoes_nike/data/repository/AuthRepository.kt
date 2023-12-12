package com.hn_2452.shoes_nike.data.repository

import android.content.Context
import android.util.Log
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.data.NikeService
import com.hn_2452.shoes_nike.data.local.NikeDatabase
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.saveStringDataByKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

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
            if(response.success) {
                Resource.success(true)
            } else {
                Resource.error(null, response.message)
            }
        }

    suspend fun changePasswordByAuthCode(email: String, authCode: String, newPassword: String) =
        withContext(Dispatchers.IO) {
            val response = NikeService.mAuthApi.changePasswordByAuthCode(email, authCode, newPassword)
            if(response.success) {
                Resource.success(true)
            } else {
                Resource.error(null, response.message)
            }
        }

    suspend fun autoLogin(token: String) =
        withContext(Dispatchers.IO) {
            val response = NikeService.mAuthApi.autoLogin(AUTH_METHOD + token)
            if(response.success) {
                Resource.success(true)
            } else {
                Resource.error(message = response.message)
            }
        }

    suspend fun changePassword(currentPassword: String, newPassword: String) =
        withContext(Dispatchers.IO) {
            val response = NikeService.mAuthApi.changePassword(currentPassword, newPassword)
            if(response.success) {
                Resource.success(true)
            } else {
                Resource.error(message = response.message)
            }
        }

}