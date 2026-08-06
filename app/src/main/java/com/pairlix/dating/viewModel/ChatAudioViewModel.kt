package com.pairlix.dating.viewModel

import android.content.Context
import android.media.MediaMetadataRetriever
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class ChatAudioViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val player = ExoPlayer.Builder(context)
        .setAudioAttributes(
            androidx.media3.common.AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
                .build(),
            true)

        .build().apply {
            volume = 1f
            playWhenReady = false
        }

    var currentUrl by mutableStateOf<String?>(null)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    var duration by mutableLongStateOf(0L)
        private set

    var position by mutableLongStateOf(0L)
        private set

    init {
        player.addListener(object : Player.Listener {

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    val d = player.duration
                    if (d > 0) duration = d
                }

                if (state == Player.STATE_ENDED) {
                    isPlaying = false
                    position = 0L
                    // ✅ NO seekTo here
                    // ✅ NO play() here
                }
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }
        })
    }

    fun toggle(url: String) {

        // ⏸ Pause if same audio is playing
        if (currentUrl == url && isPlaying) {
            player.pause()
            return
        }

        // ▶️ Replay same audio after completion
        if (currentUrl == url && !isPlaying) {
            player.seekTo(0)     // ✅ seek ONLY on user action
            player.play()
            return
        }

        // ▶️ Play new audio
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        currentUrl = url
        position = 0L
        player.play()
    }


    fun updateProgress() {
        if (player.isPlaying) {
            position = player.currentPosition
        }
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }
}