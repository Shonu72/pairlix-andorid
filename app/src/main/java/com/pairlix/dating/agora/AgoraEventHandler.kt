package com.pairlix.dating.agora

import io.agora.rtc2.IRtcEngineEventHandler

class AgoraEventHandler(
    private val onUserJoined: (Int) -> Unit,
    private val onUserLeft: () -> Unit
) : IRtcEngineEventHandler() {

    override fun onUserJoined(uid: Int, elapsed: Int) {
        onUserJoined(uid)
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        onUserLeft()
    }
}