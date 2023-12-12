package com.hn_2452.shoes_nike.ui.auth.register

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
class RegisterViewModel @Inject constructor(
    private val mAuthRepository: AuthRepository
) : ViewModel() {

    private lateinit var mRegisterInfo: RegisterInfo

    var mRegisterInfoInput: RegisterInfoInput? = null

    var mAuthCode = ""

    fun requestRegister(email: String, password: String, checkerPassword: String, acceptPolicy: Boolean) =
        liveData {

            if (email.isEmpty()) {
                emit(Resource.error(null, "Vui lòng nhập email của bạn"))
                return@liveData
            }

            if (email.isNotEmail()) {
                emit(Resource.error(null, "Vui lòng nhập đúng định dạng email"))
                return@liveData
            }

            if (password.isEmpty()) {
                emit(Resource.error(null, "Vui lòng nhập mật khẩu"))
                return@liveData
            }

            if (password.isNotPassword()) {
                emit(
                    Resource.error(
                        null,
                        "Vui lòng nhập mật khẩu từ 6 ký tự trở lên, 1 ký tự in hoa hoặc ký tự đặc biệt"
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

    class RegisterViewModelFactory(private val mAuthRepository: AuthRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RegisterViewModel(mAuthRepository) as T
            }
            throw Exception("Unable construct view_model")
        }
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