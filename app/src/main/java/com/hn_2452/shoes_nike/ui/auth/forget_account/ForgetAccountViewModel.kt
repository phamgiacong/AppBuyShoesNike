package com.hn_2452.shoes_nike.ui.auth.forget_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.isNotEmail
import com.hn_2452.shoes_nike.utility.isNotPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgetAccountViewModel @Inject constructor(
    private val mAuthRepository: AuthRepository
): ViewModel() {

    private var mCurrentEmail: String? = null

    fun findPasswordByEmail(email: String) = liveData {
        try {
            if(email.isEmpty() || email.isNotEmail()) {
                emit(Resource.error(null, "Vui lòng kiểm tra lại địa chỉ email"))
                return@liveData
            }

            mCurrentEmail = email
            emit(Resource.loading())
            emit(mAuthRepository.findPassword(email))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

    fun sendCodeAgain() = liveData {
        try {
            if(mCurrentEmail.isNullOrEmpty()) {
                emit(Resource.error(null, "Vui lòng kiểm tra lại địa chỉ email"))
                return@liveData
            }

            emit(Resource.loading())
            emit(mAuthRepository.findPassword(mCurrentEmail!!))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

    fun changePasswordByCode(authCode: String?, newPassword: String?, checkerPass: String) = liveData {
        try {

            if(authCode.isNullOrEmpty() || authCode.length < 6) {
                emit(Resource.error(message = "Mã không đúng"))
                return@liveData
            }

            if(newPassword.isNullOrEmpty() || newPassword.isNotPassword()) {
                emit(Resource.error(message = "Mật khẩu mới không hợp lệ, mật khẩu phải chứa ít nhất một ký tự đặc biệt hoặc in hoa"))
                return@liveData
            }

            if(checkerPass != newPassword) {
                emit(Resource.error(message = "Mật khẩu không khớp, vui lòng kiểm tra lại."))
                return@liveData
            }

            if(mCurrentEmail.isNullOrEmpty()) {
                emit(Resource.error(message = "Vui lòng kiểm tra lại email"))
                return@liveData
            }

            emit(Resource.loading())
            emit(mAuthRepository.changePasswordByAuthCode(mCurrentEmail!!, authCode, newPassword))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }




}