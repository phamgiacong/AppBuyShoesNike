package com.hn_2452.shoes_nike.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.model.Notification
import com.hn_2452.shoes_nike.data.repository.NotificationRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository

) : ViewModel() {
    fun getNotification() = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(notificationRepository.getAllNotification()))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }
    fun getNotificationOfUser(id:String)= liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(notificationRepository.getNotificationOfUser(id)))
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
    fun updateSeenNotification(id:String)= liveData {
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
            emit(Resource.success(notificationRepository.getQuantityNotification(id)))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }
}
