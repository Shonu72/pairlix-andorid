package com.pairlix.dating.utils

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.pairlix.dating.R

object CallManager {
    private var mediaPlayer: MediaPlayer? = null

    fun playRingtone(context: Context) {
        stopRingtone() // ✅ always clean up before creating new one
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.phone_receive_ringtone)
            if (mediaPlayer == null) {
                Log.e("CallManager", "❌ MediaPlayer.create() returned null — check R.raw.phone_receive_ringtone exists")
                return
            }
            mediaPlayer?.apply {
                isLooping = true
                setVolume(1.0f, 1.0f) // ✅ ensure full volume
                start()
                Log.e("CallManager", "✅ Ringtone started")
            }
        } catch (e: Exception) {
            Log.e("CallManager", "❌ Failed to play ringtone: ${e.message}")
        }
    }

    fun stopRingtone() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) it.stop()
                it.release()
            }
        } catch (e: Exception) {
            Log.e("CallManager", "stopRingtone error: ${e.message}")
        } finally {
            mediaPlayer = null
        }
    }
}