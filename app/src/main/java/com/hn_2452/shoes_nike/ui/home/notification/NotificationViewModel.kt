package com.hn_2452.shoes_nike.ui.home.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.model.Notification
import com.hn_2452.shoes_nike.ultils.Resource

class NotificationViewModel(
    private val mNotificationSource: List<Notification> = mutableListOf(
        Notification("1", "title", "content", 1, "1321", true),
        Notification("2", "title", "content", 1, "1321", true),
        Notification("3", "title", "content", 1, "1321", true),
        Notification("4", "title", "content", 1, "1321", true),
        Notification("5", "title", "content", 1, "1321", true),
        Notification("6", "title", "content", 1, "1321", true),
        Notification("7", "title", "content", 1, "1321", true),
    )
) : ViewModel() {
    fun getNotification() = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mNotificationSource))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

}

@Suppress("UNCHECKED_CAST")
class NotificationViewModelFactory(
    private val mNotificationSource: List<Notification>
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(mNotificationSource) as T
        }
        throw IllegalArgumentException("Unable to constructor notification view model")
    }
}