package com.pairlix.dating.firbase

import com.pairlix.dating.requests.MatchNotificationData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object NotificationBus {
    private val _events = MutableSharedFlow<Map<String, String>>()
    val events = _events.asSharedFlow()

    private val _match = MutableSharedFlow<MatchNotificationData>()
    val match = _match.asSharedFlow()

    suspend fun post(data: Map<String, String>) {
        _events.emit(data)
    }

    suspend fun sendMatch(data: MatchNotificationData) {
        _match.emit(data)
    }
}