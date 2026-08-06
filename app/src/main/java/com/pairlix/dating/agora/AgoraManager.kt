package com.pairlix.dating.agora

import android.content.Context
import android.util.Log
import com.pairlix.dating.helper.CONSTANT.APP_ID
import io.agora.rtc2.*
import io.agora.rtc2.video.VideoCanvas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.media.AudioManager

object AgoraManager {
    var rtcEngine: RtcEngine? = null
    private val _remoteUsers = MutableStateFlow<List<Int>>(emptyList())
    val remoteUsers: StateFlow<List<Int>> = _remoteUsers
    fun init(context: Context) {
        if (rtcEngine != null) {
            // Already initialized, just ensure clean state
            _remoteUsers.value = emptyList()
            return
        }

        val appId = "2889363aa8b14ad0bf861c5eb2bb0d25"
        rtcEngine = RtcEngine.create(
            context,
            appId,
            object : IRtcEngineEventHandler() {
                override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                    Log.e("AGORA_EVENT", "✅✅✅ JOIN SUCCESS ✅✅✅")
                    Log.e("AGORA_EVENT", "My UID: $uid, Channel: $channel")
                }
                override fun onUserJoined(uid: Int, elapsed: Int) {
                    Log.e("AGORA_EVENT", "👤👤👤 REMOTE USER JOINED: $uid 👤👤👤")
                    // Add to remote users list
                    _remoteUsers.value = _remoteUsers.value + uid
                    Log.e("AGORA_EVENT", "Remote users list: ${_remoteUsers.value}")
                }
                override fun onUserOffline(uid: Int, reason: Int) {
                    Log.e("AGORA_EVENT", "👤 Remote user left: $uid, reason: $reason")
                    // Remove from remote users list
                    _remoteUsers.value = _remoteUsers.value - uid
                }
                override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
                    Log.e("AGORA_EVENT", "📹 Remote video state changed: uid=$uid, state=$state, reason=$reason")
                }
                override fun onError(err: Int) {
                    Log.e("AGORA_EVENT", "❌ ERROR: $err")
                }

            }
        )
        Log.e("AGORA_INIT", "✅ Engine initialized")
    }
    fun switchCamera() {
        rtcEngine?.switchCamera()
    }
    fun muteVideo(mute: Boolean) {
        rtcEngine?.muteLocalVideoStream(mute)
    }
    fun muteMic(mute: Boolean) {
        rtcEngine?.muteLocalAudioStream(mute)
    }
    fun leaveCall() {
        rtcEngine?.leaveChannel()
        _remoteUsers.value = emptyList()
    }

    var speakerEnabled = false

    fun toggleSpeaker(context: Context): Boolean {

        speakerEnabled = !speakerEnabled

        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION

        if (speakerEnabled) {
            // ✅ Speaker ON
            audioManager.isSpeakerphoneOn = true
            rtcEngine?.setEnableSpeakerphone(true)

            Log.e("AGORA_AUDIO", "🔊 Speaker ON")
        } else {

            audioManager.isSpeakerphoneOn = false
            rtcEngine?.setEnableSpeakerphone(false)
            Log.e("AGORA_AUDIO", "📞 Speaker OFF (Earpiece)")
        }

        return speakerEnabled
    }


    fun destroy() {
        _remoteUsers.value = emptyList()
        RtcEngine.destroy()
        rtcEngine = null
    }
}
