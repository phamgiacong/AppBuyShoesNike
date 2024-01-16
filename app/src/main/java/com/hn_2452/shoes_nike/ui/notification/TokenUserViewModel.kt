package com.hn_2452.shoes_nike.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.TokenRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton


@HiltViewModel
class TokenUserViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : ViewModel() {

    fun sendRegistrationToServer(token: String, useId: String?) = liveData {
        if (token.isEmpty()) {
            emit(Resource.error(message = "token error"))
            return@liveData
        }
        if (useId.isNullOrEmpty()) {
            emit(Resource.error(message = "userId error"))
        }
        emit(Resource.loading())
        emit(tokenRepository.sendRegistrationTokenToServer(useId, token))
    }

    fun deleteToken(token: String) {

    }

}