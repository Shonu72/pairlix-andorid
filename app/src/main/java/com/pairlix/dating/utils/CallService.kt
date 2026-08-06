package com.pairlix.dating.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.SurfaceView
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.pairlix.dating.R
import com.pairlix.dating.agora.AgoraManager
import com.pairlix.dating.agora.AgoraManager.rtcEngine
import io.agora.rtc2.Constants
import io.agora.rtc2.video.VideoCanvas


class CallService : Service() {

    companion object {
        const val CHANNEL_ID = "call_service_channel"
        const val NOTIFICATION_ID = 101
        const val ACTION_START_CALL = "START_CALL"
        const val ACTION_END_CALL = "END_CALL"
        const val EXTRA_TOKEN = "token"
        const val EXTRA_CHANNEL = "channel"
        const val EXTRA_UID = "uid"
        const val Call_Type = "call_type"
    }

    private var isJoined = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_CALL -> {
                isJoined = false
                val token = intent.getStringExtra(EXTRA_TOKEN) ?: ""
                val channel = intent.getStringExtra(EXTRA_CHANNEL) ?: ""
                val uid = intent.getIntExtra(EXTRA_UID, 0)
                val callType = intent.getStringExtra(Call_Type) ?: ""

                if (!startForegroundSafely(callType)) {
                    Log.e("CallService", "❌ Could not start foreground — missing permissions")
                    stopSelf()
                    return START_NOT_STICKY
                }

                if (token.isNotBlank() && channel.isNotBlank()) {
                    Log.e("token", "token ${token}  channel ${channel}: ", )
                    initAgora(callType)
                    joinChannel(token, channel, uid)

                } else {
                    Log.e("CallService", "❌ Token or channel is blank")
                    stopSelf()
                    return START_NOT_STICKY // ✅ Fixed
                }
            }

            ACTION_END_CALL -> {
                Log.e("calllls", "ca;;;;;" )
                endCall()
                stopSelf()
                return START_NOT_STICKY // ✅ Fixed

            }
        }
        return START_STICKY
    }



    /**
     * Starts foreground with only the service types whose runtime
     * permissions are actually granted right now.
     * Returns false if even RECORD_AUDIO is missing (nothing we can do).
     */
    private fun startForegroundSafely(callType: String): Boolean {
        val audioGranted = checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED

        val cameraGranted = checkSelfPermission(android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED

        // RECORD_AUDIO is mandatory for any call — cannot proceed without it
        if (!audioGranted) {
            Log.e("CallService", "❌ RECORD_AUDIO permission not granted")
            return false
        }

        val serviceType = when {
            callType == "video" && cameraGranted ->
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE or
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA

            else -> {
                // Audio-only call, or video call where camera permission is missing
                if (callType == "video" && !cameraGranted) {
                    Log.w("CallService", "⚠️ Video call but CAMERA not granted — falling back to audio")
                }
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
            }
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Call in progress")
            .setContentText("Pairlix call running")
            .setSmallIcon(R.drawable.pairlix_logo)
            .setOngoing(true)
            .setCategory(Notification.CATEGORY_CALL)
            .build()

        ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, serviceType)
        Log.d("CallService", "✅ Foreground started — type: $serviceType")
        return true
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Call Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Active call notification"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun initAgora(callType: String) {
        AgoraManager.init(applicationContext)

        AgoraManager.rtcEngine?.apply {
            setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)
            Log.e("calltype", "${callType}", )

            if (callType == "video") {

                enableVideo()
                enableAudio()

                val surfaceView = SurfaceView(this@CallService)
                setupLocalVideo(
                    VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0)
                )

                startPreview()

                Log.d("CallService", "✅ Agora init — video mode")
            }
            else {
                disableVideo()
                enableAudio()

                stopPreview()
                Log.d("CallService", "✅ Agora init — audio mode")
            }

        }
    }

    private fun joinChannel(token: String, channel: String, uid: Int) {
        if (isJoined) {
            Log.w("CallService", "⚠️ Already joined channel, skipping")
            return
        }

        val result = AgoraManager.rtcEngine?.joinChannel(token, channel, null, uid)

        if (result == 0) {
            isJoined = true
            Log.d("CallService", "✅ Joined channel: $channel uid: $uid")
        } else {
            Log.e("CallService", "❌ joinChannel failed with code: $result")
        }
    }

    private fun endCall() {
        if (isJoined) {
            AgoraManager.rtcEngine?.leaveChannel()
            isJoined = false
            Log.d("CallService", "✅ Left channel")
        }
        AgoraManager.destroy()
    }

    override fun onDestroy() {

        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
        isJoined=false
        endCall()
        CallManager.stopRingtone()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}