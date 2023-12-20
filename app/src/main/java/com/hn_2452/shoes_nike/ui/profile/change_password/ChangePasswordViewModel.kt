package com.hn_2452.shoes_nike.ui.profile.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.isNotPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val mAuthRepository: AuthRepository
) : ViewModel() {
    fun changePassword(currentPass: String?, newPass: String?, reNewPass: String?) = liveData {
        try {
            if (currentPass.isNullOrEmpty() || currentPass.isNotPassword()) {
                emit(Resource.error(message = "Mật khẩu hiện tại không hợp lệ"))
                return@liveData
            }

            if (newPass.isNullOrEmpty() || newPass.isNotPassword()) {
                emit(Resource.error(message = "Mật khẩu mới không hợp lệ, mật khẩu phải chứa ít nhất một ký tự đặc biệt hoặc in hoa"))
                return@liveData
            }

            if (newPass != reNewPass) {
                emit(Resource.error(message = "Mật khẩu không khớp, vui lòng kiểm tra lại."))
                return@liveData
            }

            emit(Resource.loading())
            emit(mAuthRepository.changePassword(currentPass, newPass))
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

}