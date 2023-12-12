package com.hn_2452.shoes_nike.ui.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class NotificationService: FirebaseMessagingService() {
    companion object {
        const val TAG = "Nike:NotificationService: "
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "onNewToken: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {

    }

}