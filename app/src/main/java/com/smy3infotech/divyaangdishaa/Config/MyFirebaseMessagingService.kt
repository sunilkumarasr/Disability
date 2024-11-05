package com.smy3infotech.divyaangdishaa.Config
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM messages here.
        Log.e("FCM", "From: ${remoteMessage.from}")
        Log.e("SUnil Test", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.e("SUnil Test", "Message data payload: ${remoteMessage.data}")
            // Process custom data here
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.e("SUnil Test", "Message Notification Body: ${it.body}")
            // Show notification here
        }
    }

    override fun onNewToken(token: String) {
        Log.e("SUnil Test", "New token: $token")
        // Send token to your server if necessary
    }
}