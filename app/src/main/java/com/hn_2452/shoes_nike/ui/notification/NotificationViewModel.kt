package com.hn_2452.shoes_nike.ui.notification

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.TOKEN_METHOD
import com.hn_2452.shoes_nike.data.model.Notification
import com.hn_2452.shoes_nike.data.repository.NotificationRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    @ApplicationContext private val mContext: Context
) : ViewModel() {
    fun getNotification() = liveData {
        emit(Resource.loading(null))
        try {
            emit(notificationRepository.getAllNotification())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }
    fun getNotificationOfUser()= liveData {
        try {
            val token = getStringDataByKey(mContext, TOKEN)
            if(token.isEmpty()) {
                emit(Resource.error(message = "token is not valid"))
                return@liveData
            }
            emit(Resource.loading())
            emit(notificationRepository.getNotificationOfUser(TOKEN_METHOD + token))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }
    fun getNotificationOffer() = liveData {
        emit(Resource.loading(null))
        try {
            emit(notificationRepository.getNotificationOffer())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }
    fun updateSeenNotification(id:String?)= liveData {
        if(id.isNullOrEmpty()){
            emit(Resource.error(message = "id error"))
            return@liveData
        }
        emit(Resource.loading())
        emit(notificationRepository.updateSeenNotification(id))
    }
    fun getQuantityNotification(id:String)= liveData {
        emit(Resource.loading(null))
        try {
            emit(notificationRepository.getQuantityNotification(id))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }
}
