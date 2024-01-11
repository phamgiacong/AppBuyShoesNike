package com.hn_2452.shoes_nike.ui.profile.change_password

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.TOKEN_METHOD
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import com.hn_2452.shoes_nike.utility.handleEx
import com.hn_2452.shoes_nike.utility.isNotPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val mAuthRepository: AuthRepository,
    @ApplicationContext private val mContext: Context
) : ViewModel() {
    fun changePassword(currentPass: String?, newPass: String?, reNewPass: String?) = liveData(
        handleEx(mContext)
    ) {
        try {
            val token = TOKEN_METHOD + getStringDataByKey(mContext, TOKEN)

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
            emit(mAuthRepository.changePassword(token, currentPass, newPass))
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

}