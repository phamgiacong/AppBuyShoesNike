package com.hn_2452.shoes_nike.ui.auth.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.isNotEmail
import com.hn_2452.shoes_nike.utility.isNotPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val mAuthRepository: AuthRepository
) : ViewModel() {

    private lateinit var mRegisterInfo: RegisterInfo

    var mRegisterInfoInput: RegisterInfoInput? = null

    var mAuthCode = ""

    fun requestRegister(
        email: String,
        password: String,
        checkerPassword: String,
        acceptPolicy: Boolean
    ) =
        liveData {

            if (email.isEmpty()) {
                emit(Resource.error(null, "Vui lòng nhập email của bạn"))
                return@liveData
            }

            if (email.isNotEmail()) {
                emit(
                    Resource.error(
                        null,
                        mContext.getString(R.string.request_enter_a_valid_email_msg)
                    )
                )
                return@liveData
            }

            if (password.isEmpty()) {
                emit(
                    Resource.error(
                        null,
                        mContext.getString(R.string.request_enter_password_message)
                    )
                )
                return@liveData
            }

            if (password.isNotPassword()) {
                emit(
                    Resource.error(
                        null,
                        mContext.getString(R.string.error_password_message)
                    )
                )
                return@liveData
            }

            if (checkerPassword != password) {
                emit(Resource.error(null, "Mật khẩu không khớp"))
                return@liveData
            }

            if (!acceptPolicy) {
                emit(Resource.error(null, "Vui lòng chọn đã hiểu rõ điều khoản"))
                return@liveData
            }

            emit(Resource.loading())
            mRegisterInfo = RegisterInfo(email, password)
            emit(mAuthRepository.requestRegister(email))
        }

    fun registerAgain() = liveData {
        emit(Resource.loading())
        emit(mAuthRepository.requestRegister(mRegisterInfo.email))
    }

    fun register() = liveData {
        emit(Resource.loading())
        emit(mAuthRepository.register(mRegisterInfo.email, mRegisterInfo.password))
    }

    data class RegisterInfo(
        var email: String,
        var password: String
    )

    data class RegisterInfoInput(
        var email: String,
        var password: String,
        var checkerPassword: String,
        var acceptPolicy: Boolean
    )

}