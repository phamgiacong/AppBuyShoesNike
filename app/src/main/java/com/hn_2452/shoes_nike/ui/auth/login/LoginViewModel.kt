package com.hn_2452.shoes_nike.ui.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.isNotEmail
import com.hn_2452.shoes_nike.utility.isNotPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mAuthRepository: AuthRepository
) : ViewModel() {

    companion object {
        const val FACEBOOK_ACCOUNT = 1
        const val GOOGLE_ACCOUNT = 2
        const val TAG = "Nike:LoginViewModel: "

    }


    fun login(email: String, password: String) = liveData {
        try {
            if (email.isEmpty()) {
                emit(Resource.error(false, "Vui lòng nhập email"))
                return@liveData
            }

            if(email.isNotEmail()) {
                emit(Resource.error(false, "Vui lòng nhập đúng định dạng email"))
                return@liveData
            }

            if (password.isEmpty()) {
                emit(Resource.error(false, "Vui lòng nhập mật khẩu"))
                return@liveData
            }

            if (password.isNotPassword()) {
                emit(Resource.error(false, "Vui lòng nhập đúng định dạng mật khẩu"))
                return@liveData
            }

            emit(Resource.loading())
            emit(mAuthRepository.login(email, password))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }

    }


     fun loginWithFacebook(accountId: String?, displayName: String?, avatar: String?) =
        liveData {
            try {
                if (accountId.isNullOrBlank() || displayName.isNullOrBlank() || avatar.isNullOrBlank()) {
                    emit(Resource.error(null, "Tài khoản Facebook không khả dụng"))
                    return@liveData
                }
                emit(Resource.loading(null))
                emit(
                    mAuthRepository.loginWith(
                        FACEBOOK_ACCOUNT,
                        accountId,
                        displayName,
                        avatar,
                        null
                    )
                )
            } catch (ex: Exception) {
                emit(Resource.error(null, ex.message!!))
            }
        }

    fun loginWithGoogle(
        accountId: String?,
        displayName: String?,
        avatar: String?,
        email: String?
    ) = liveData {
        try {
            if (accountId.isNullOrBlank() || displayName.isNullOrBlank() || avatar.isNullOrBlank()) {
                emit(Resource.error(null, "Tài khoản Google không khả dụng"))
                return@liveData
            }
            emit(Resource.loading(null))
            emit(mAuthRepository.loginWith(GOOGLE_ACCOUNT, accountId, displayName, avatar, email))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

    class LoginViewModelFactory(private val mAuthRepository: AuthRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(mAuthRepository) as T
            }
            throw Exception("Unable construct view_model")
        }
    }

}