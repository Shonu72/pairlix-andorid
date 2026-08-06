package com.pairlix.dating.firbase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.requests.MatchNotificationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val contextRef: WeakReference<Context> = WeakReference(this)
    override fun onNewToken(token: String) {

    }
    override fun handleIntent(intent: Intent) {

        try {

            if (intent.extras != null) {
                val builder = RemoteMessage.Builder("MyFirebaseMessagingService")

                for (key in intent.extras!!.keySet()) {

                    builder.addData(key!!, intent.extras!![key].toString())

                }

                onMessageReceived(builder.build())

            } else {

                super.handleIntent(intent)

            }

        } catch (e: java.lang.Exception) {

            super.handleIntent(intent)

        }

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("Notui", "${remoteMessage.notification}: ", )
        val title = remoteMessage.notification?.title ?: "Pairlix"
        val body = remoteMessage.notification?.body ?: ""
        val data = remoteMessage.data

        val intent = Intent("com.pairlix.NOTIFICATION_RECEIVED").apply {
            putExtra("type", data["type"] ?: "") }
        sendBroadcast(intent)

        Log.e("FCM", "onMessageReceived called, data=$data")
        Log.e("NotificationData", "$data ", )
        Log.e("FCM", "Broadcast sent with type=${data["type"]}")
        sendNotificationData(this, body, title,data)

    }


    private fun sendNotificationData(
        context: Context?,
        body: String?,
        title: String?,
        data: Map<String, String>
    ) {
        var intent: Intent?=null
        if (data["type"]=="INCOMING_CALL") {
             intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                // 🔥 ALL DATA PASS KARO
                data.forEach { (key, value) ->
                    putExtra(key, value)
                }

                putExtra("noti", "noti")

            }
        }

        else{
            if (data["type"] == "MATCH") {
                val matchData = MatchNotificationData(
                    senderName = data["senderName"] ?: "",
                    senderImage = data["senderImage"] ?: "",
                    senderAge = data["senderAge"] ?: "",
                    senderOnline = data["senderOnline"] == "true",
                    senderFaceVerified = data["senderFaceVerified"] == "true",
                    senderDocumentVerified = data["senderDocumentVerified"] == "true",
                    receiverName = data["receiverName"] ?: "",
                    receiverImage = data["receiverImage"] ?: "",
                    receiverAge = data["receiverAge"] ?: "",
                    receiverOnline = data["receiverOnline"] == "true",
                    receiverFaceVerified = data["receiverFaceVerified"] == "true",
                    receiverDocumentVerified = data["receiverDocumentVerified"] == "true",
                    matchId = data["id"] ?: "",
                    matchedOn = data["matchedOn"] ?: ""
                )

                CoroutineScope(Dispatchers.IO).launch {
                    NotificationBus.sendMatch(matchData)
                }
            }
            else {
                CoroutineScope(Dispatchers.IO).launch {
                    NotificationBus.post(data)
                }
            }
             intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

                data.forEach { (key, value) ->
                    putExtra(key, value)
                }
                putExtra("noti", "noti")

            }
        }


        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.enableVibration(true)

            notificationManager.createNotificationChannel(channel)
        }


        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.pairlix_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            builder.build()
        )
    }

}