package com.hn_2452.shoes_nike

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.hn_2452.shoes_nike.ui.notification.TokenUserViewModel

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NikeShoesApplication : Application(){
    companion object{
        val CHANEL_ID ="push_notification_id"
    }
    override fun onCreate() {
        super.onCreate()
        createChanelNotification()
        Log.e("TAG", "onCreate: ", )
    }

    private fun createChanelNotification() {
        Log.e("TAG", "createChanelNotification: ", )
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel =
                NotificationChannel(CHANEL_ID,"Pushnotification",NotificationManager.IMPORTANCE_DEFAULT) as NotificationChannel
            val manager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("TAG",token.toString() )

        })
    }


}