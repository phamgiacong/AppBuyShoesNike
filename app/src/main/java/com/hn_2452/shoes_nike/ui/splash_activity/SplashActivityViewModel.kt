package com.hn_2452.shoes_nike.ui.splash_activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.handleEx
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(
    private val mAuthRepository: AuthRepository,
    @ApplicationContext private val mContext: Context
): ViewModel() {

    fun autoLogin(token: String) = liveData(handleEx(mContext)) {
        try {
            emit(Resource.loading())
            emit(mAuthRepository.autoLogin(token))
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

}