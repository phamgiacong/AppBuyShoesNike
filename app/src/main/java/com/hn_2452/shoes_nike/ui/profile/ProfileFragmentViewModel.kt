package com.hn_2452.shoes_nike.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.data.local.NikeDatabase
import com.hn_2452.shoes_nike.utility.saveStringDataByKey
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    @ApplicationContext private val mApp: Context,
    private val mNikeDatabase: NikeDatabase
) : ViewModel() {

    val mUsers = mNikeDatabase.userDao().getUsers()
    fun logout() = liveData {
        try {
            saveStringDataByKey(mApp, TOKEN, "")
            mNikeDatabase.userDao().deleteUser()
            emit(true)
        } catch (ex: Exception) {
            emit(false)
        }
    }

}
