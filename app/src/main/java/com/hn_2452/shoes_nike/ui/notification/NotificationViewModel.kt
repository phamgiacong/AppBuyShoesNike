package com.hn_2452.shoes_nike.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.model.Notification
import com.hn_2452.shoes_nike.data.repository.NotificationRepository
import com.hn_2452.shoes_nike.utility.Resource
import javax.inject.Inject

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
}
