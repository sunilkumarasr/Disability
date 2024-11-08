package com.smy3infotech.divyaangdish.Config
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("FCM", "From: ${remoteMessage.from}")

        remoteMessage.data.isNotEmpty().let {
            Log.e("FCM", "Data payload: ${remoteMessage.data}")
        }

        remoteMessage.notification?.let {
            Log.e("FCM", "Notification Body: ${it.body}")
        }
    }


    override fun onNewToken(token: String) {
        Log.e("SUnil Test", "New token: $token")
        // Send token to your server if necessary
    }
}