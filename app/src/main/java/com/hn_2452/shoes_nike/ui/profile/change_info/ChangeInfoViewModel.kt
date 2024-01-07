package com.hn_2452.shoes_nike.ui.profile.change_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.saveStringDataByKey
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ChangeInfoViewModel @Inject constructor(
    private val mAuthRepository: AuthRepository
) : ViewModel() {
    fun updateUser(
        image: MultipartBody.Part,
        name: RequestBody?,
        fullName: RequestBody?,
        birthDay: RequestBody?,
        gender: RequestBody?,
        phoneNumber: RequestBody?
    ) = liveData {
        try {
            emit(Resource.loading())
            emit(mAuthRepository.uploadUserInfo(image, name, fullName, birthDay, gender, phoneNumber))
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

    fun loadUserData() = liveData {
        try {
            emit(Resource.loading())
            emit(mAuthRepository.getUserInfo())
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }
}