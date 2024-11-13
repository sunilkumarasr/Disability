package com.smy3infotech.divyaangdisha.Config

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.smy3infotech.divyaangdisha.Activitys.NotificationActivity
import com.smy3infotech.divyaangdisha.R
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.System.currentTimeMillis

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val channelId = "Disability_channel" // Define a notification channel ID

    companion object {
        lateinit var sharedPreferences: SharedPreferences
        private val TAG = "MyFirebaseToken"
        private lateinit var notificationManager: NotificationManager
        private  var title: String=""
        private  var notification_body: String=""
        private  var userid: String=""
        var token: String? = ""
        var body: String? = ""
        var id: String? = ""
        //var context: Context
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.e("onMessageReceived: ", p0.data.toString())

        Log.e("onMessageReceived: ", p0.notification.toString())

        if (p0.data.isNotEmpty()) {
            try {
                val params: Map<String?, String?> = p0.data
                val json = JSONObject(params)

                val message = json.getString("message")
                var obj = JSONObject(message)
                val jsonString = message.substring(1, message.length - 1).replace("\\", "")
                val jsonObject = JSONTokener(message).nextValue() as JSONObject

                id = jsonObject.getString("id")
                title = jsonObject.getString("title")
                body = jsonObject.getString("body")
//
//                if ((id.equals("1"))){
//                    startActivity(Intent(context,Home_Screen::class.java))
//                }

                Log.e(TAG, "onMessageReceived: $json")
                Log.e("jsonString", "" + jsonString)
                Log.e("id", "" + id)


                val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

//        Log.e("tilte", "" + title)
                val intent = Intent(this, NotificationActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                val pendingIntent: PendingIntent
                pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                } else {
                    PendingIntent.getActivity(
                        this,
                        0, intent, PendingIntent.FLAG_IMMUTABLE
                    )
                }
                Log.e("id", " 13 " + id)

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    checkNotificationChannel("Disability_Properties")
                }

                Log.e("id", " 14 " + id)

                val notification = NotificationCompat.Builder(applicationContext, "Disability_channel")
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val color = ContextCompat.getColor(this, R.color.colorPrimary)
                    notification.setColor(
                        Color.RED

                    )
//            notification.setColor(ContextCompat.getColor(this, R.color.red))


                } else {
                    val color = ContextCompat.getColor(this, R.color.colorPrimary)
                    notification.setColor(
                        Color.RED


                    )
//            notification.setColor(ContextCompat.getColor(this, R.color.red))
                }
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Disability")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setStyle(
                        NotificationCompat.MessagingStyle(title!!)
                            .setGroupConversation(false)
                            .addMessage(body, currentTimeMillis(), title)
                    )
                    //.setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSound(defaultSound)
                Log.e("id", " 15 " + id)

                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1, notification.build())
                Log.e("id", " 16 " + id)

            } catch (e: JSONException) {
                Log.e(TAG, "Exception: " + e.message)
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkNotificationChannel(CHANNEL_ID: String) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "Disability_channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = body
        notificationChannel.enableLights(true)

        notificationChannel.enableVibration(true)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onNewToken(p0: String) {
        token = p0
        super.onNewToken(p0)
    }

    private fun getProfile() {

    }
}