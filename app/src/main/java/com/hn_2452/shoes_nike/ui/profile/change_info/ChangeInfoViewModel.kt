package com.hn_2452.shoes_nike.ui.profile.change_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.data.local.NikeDatabase
import com.hn_2452.shoes_nike.data.model.User
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.saveStringDataByKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ChangeInfoViewModel @Inject constructor(
    private val mAuthRepository: AuthRepository,
    private val mNikeDatabase: NikeDatabase
) : ViewModel() {
    fun updateUser(
        image: String?,
        fullName: String?,
        birthDay: String?,
        gender: String?,
        phoneNumber: String?
    ) = liveData {
        try {
            emit(Resource.loading())
            emit(mAuthRepository.uploadUserInfo(image, fullName, birthDay, gender, phoneNumber))
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

    fun updateUser(user: User) = viewModelScope.launch {
        mNikeDatabase.userDao().deleteUser()
        mNikeDatabase.userDao().addUser(user)
    }
}