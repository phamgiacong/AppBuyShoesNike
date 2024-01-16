package com.hn_2452.shoes_nike.ui.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hn_2452.shoes_nike.NEW_SHOES_NOTIFY
import com.hn_2452.shoes_nike.NOTIFICATION_ACTION
import com.hn_2452.shoes_nike.NOTIFY_OBJECT_ID
import com.hn_2452.shoes_nike.NOTIFY_TYPE
import com.hn_2452.shoes_nike.NikeShoesApplication
import com.hn_2452.shoes_nike.OFFER_NOTIFY
import com.hn_2452.shoes_nike.ORDER_NOTIFY
import com.hn_2452.shoes_nike.OTHER_NOTIFY
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.repository.TokenRepository
import com.hn_2452.shoes_nike.utility.getBooleanDataByKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var mToKenRepository: TokenRepository

    companion object {
        const val TAG = "Nike:NotificationService: "
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val type = message.data["type"] ?: ""
        val title: String? = message.data["title"]
        val content: String? = message.data["body"]
        val id: String? = message.data["id"]
        val image: String? = message.data["image"]

        var bitmap: Bitmap? = null
        try {
            bitmap = getBitmapfromUrl(image)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (type == ORDER_NOTIFY && getBooleanDataByKey(this, ORDER_NOTIFY)) {
            sendOrderNotification(id, title, content)
            return
        }

        if (type == OFFER_NOTIFY && getBooleanDataByKey(this, OFFER_NOTIFY)) {
            sendOfferNotification(id, title, content, bitmap)
            return
        }

        if (type == NEW_SHOES_NOTIFY && getBooleanDataByKey(this, NEW_SHOES_NOTIFY)) {
            sendNewShoesNotification(id, title, content, bitmap)
            return
        }

        if (type == "" && getBooleanDataByKey(this, OTHER_NOTIFY)) {
            sendOtherNotification(title, content)
            return
        }
    }

    private fun sendOtherNotification(
        title: String?,
        content: String?
    ) {
        Log.i(TAG, "sendOrderNotification: title=$title content==$content")
        val notificationId = Random.nextInt()
        val data = Bundle().apply {
            putString(NOTIFY_TYPE, OTHER_NOTIFY)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(NOTIFICATION_ACTION).apply {
                putExtras(data)
            },
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder = NotificationCompat.Builder(this, NikeShoesApplication.CHANEL_ID)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSmallIcon(R.drawable.logo)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager?)?.notify(
            notificationId,
            notificationBuilder.build()
        )
    }

    private fun sendOrderNotification(id: String?, title: String?, content: String?) {
        Log.i(TAG, "sendOrderNotification: id=$id title=$title content==$content")
        val notificationId = Random.nextInt()
        val data = Bundle().apply {
            putString(NOTIFY_OBJECT_ID, id)
            putString(NOTIFY_TYPE, ORDER_NOTIFY)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(NOTIFICATION_ACTION).apply {
                putExtras(data)
            },
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder = NotificationCompat.Builder(this, NikeShoesApplication.CHANEL_ID)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSmallIcon(R.drawable.logo)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager?)?.notify(
            notificationId,
            notificationBuilder.build()
        )

    }

    private fun sendNewShoesNotification(
        id: String?,
        title: String?,
        content: String?,
        bitmap: Bitmap?
    ) {
        Log.i(TAG, "sendOrderNotification: id=$id title=$title content==$content")
        val notificationId = Random.nextInt()
        val data = Bundle().apply {
            putString(NOTIFY_OBJECT_ID, id)
            putString(NOTIFY_TYPE, NEW_SHOES_NOTIFY)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(NOTIFICATION_ACTION).apply {
                putExtras(data)
            },
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder = NotificationCompat.Builder(this, NikeShoesApplication.CHANEL_ID)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSmallIcon(R.drawable.logo)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setLargeIcon(bitmap)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(bitmap)
                    .bigLargeIcon(getBitmapfromUrl(""))
            )

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager?)?.notify(
            notificationId,
            notificationBuilder.build()
        )

    }


    private fun sendOfferNotification(
        id: String?,
        title: String?,
        content: String?,
        bitmap: Bitmap?
    ) {
        val notificationId = Random.nextInt()
        val data = Bundle().apply {
            putString(NOTIFY_OBJECT_ID, id)
            putString(NOTIFY_TYPE, OFFER_NOTIFY)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(NOTIFICATION_ACTION).apply {
                putExtras(data)
            },
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder = NotificationCompat.Builder(this, NikeShoesApplication.CHANEL_ID)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSmallIcon(R.drawable.logo)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setLargeIcon(bitmap)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(bitmap)
                    .bigLargeIcon(getBitmapfromUrl(""))
            )

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager?)?.notify(
            notificationId,
            notificationBuilder.build()
        )

    }

    private fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Error in getting notification image: " + e.localizedMessage)
            null
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e(TAG, "onNewToken: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        GlobalScope.launch {
            mToKenRepository.sendRegistrationTokenToServer(null, token)
        }

    }

}