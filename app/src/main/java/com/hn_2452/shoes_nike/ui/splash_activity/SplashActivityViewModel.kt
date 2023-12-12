package com.hn_2452.shoes_nike.ui.splash_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(
    private val mAuthRepository: AuthRepository
): ViewModel() {

    fun autoLogin(token: String) = liveData {
        try {
            emit(Resource.loading())
            emit(mAuthRepository.autoLogin(token))
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

}