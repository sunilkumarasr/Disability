package com.smy3infotech.divyaangdisha

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.smy3infotech.divyaangdisha.Activitys.NotificationActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    var title: String = ""
    var body: String = ""
    var type: String = ""
    var product_name: String = ""
    var product_id: String = ""

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("onMessageReceived: ", remoteMessage.data.toString())

        if (remoteMessage.data.isNotEmpty()) {
            val data = remoteMessage.data

            title = data["title"] ?: "Notification"
            body = data["body"] ?: "You have a new message"
            type = data["type"] ?: "0"
            product_name = data["product_name"] ?: "0"
            product_id = data["product_id"] ?: "0"

            Log.e("FCM", "Title: $title, Body: $body, Product ID: $product_id")

            createNotification()
        }

    }

    private fun createNotification() {

        val intent = Intent(this, NotificationActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val channelId = "1"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            checkNotificationChannel(channelId)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo_name_ic)
            .setContentTitle("Disability")
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(pendingIntent, true)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkNotificationChannel(channelId: String) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            channelId,
            "Stranger Sparks",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = body
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FCM Token", token)
    }

}