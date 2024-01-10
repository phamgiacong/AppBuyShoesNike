package com.hn_2452.shoes_nike.ui.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hn_2452.shoes_nike.BASE_URL
import com.hn_2452.shoes_nike.MainActivity
import com.hn_2452.shoes_nike.NikeShoesApplication
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.ui.splash_activity.SplashActivity
import java.net.HttpURLConnection
import java.net.URL


class NotificationService: FirebaseMessagingService() {
    companion object {
        const val TAG = "Nike:NotificationService: "
    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if(message.notification!=null){
            var bitmap: Bitmap?= null
            try {
               bitmap = getBitmapfromUrl(BASE_URL+message.notification!!.imageUrl.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val notification : RemoteMessage.Notification? =  message.notification
                val strTitle : String? = notification?.title
                val strBody :String? = notification?.body
                val data : Map<String,String> = message.data
                val id :String? = data.get("id")
                val type:String? = data.get("type")
                sendNotification(strTitle,strBody,bitmap,id,type)
            }catch (e:Exception){
            }
        }
    }
    private fun sendNotification(title:String? , body:String?,bitmap: Bitmap?,id:String? , type:String?){
        val bundle=Bundle()
        bundle.putString("id",id)
        bundle.putString("type",type)

        Log.e(TAG, "sendNotification: $type")
        val intent  = Intent(this,MainActivity::class.java)
        intent.putExtras(bundle)
        val pendingIntent  = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE)
         val notificationBuilder = NotificationCompat.Builder(this,NikeShoesApplication.CHANEL_ID)
             .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
             .setSmallIcon(R.drawable.logo)
             .setContentIntent(pendingIntent)
             .setLargeIcon(bitmap).setStyle(NotificationCompat.BigPictureStyle()
                   .bigLargeIcon(bitmap)
                   .setBigContentTitle(title)
                   .setSummaryText(body))
        val notification = notificationBuilder.build()
        val manager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(manager!=null){
            manager.notify(1,notification)
        }

    }
    fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: java.lang.Exception) {
            Log.e("awesome", "Error in getting notification image: " + e.localizedMessage)
            null
        }
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e(TAG, "onNewToken: $token")
        sendRegistrationToServer(token)
    }
    private fun sendRegistrationToServer(token: String) {

    }

}