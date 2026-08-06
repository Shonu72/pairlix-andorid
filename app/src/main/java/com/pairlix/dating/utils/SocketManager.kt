package com.pairlix.dating.utils

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class SocketManager @Inject constructor() {

    private val TAG = "SocketManager"
    private var socket: Socket? = null
    private var isInitialized = false

    private val _socketState = MutableStateFlow(SocketState.DISCONNECTED)
    val socketState = _socketState.asStateFlow()

    fun init(userId: String) {
        try {
            // If already connected, just emit online and return
            if (socket?.connected() == true) {
                Log.d(TAG, "Socket already connected for user: $userId")
                emitOnline(userId)
                return
            }

            // If socket exists but disconnected, clean it up first
            if (socket != null) {
                cleanupSocket()
            }

            _socketState.value = SocketState.CONNECTING
            Log.d(TAG, "Initializing socket for user: $userId")

            val options = IO.Options().apply {
                transports = arrayOf("websocket")
                reconnection = true
                reconnectionAttempts = 5
                reconnectionDelay = 1000
                timeout = 10000
            }

            socket = IO.socket("http://43.205.51.214:3000", options).apply {

                on(Socket.EVENT_CONNECT) {
                    Log.d(TAG, "Socket connected successfully")
                    _socketState.value = SocketState.CONNECTED
                    isInitialized = true
                    emitOnline(userId)
                }


                on(Socket.EVENT_DISCONNECT) { args ->
                    Log.d(TAG, "Socket disconnected: ${args.firstOrNull()}")
                    _socketState.value = SocketState.DISCONNECTED
                }

                on(Socket.EVENT_CONNECT_ERROR) { args ->
                    Log.e(TAG, "Socket connection error: ${args.firstOrNull()}")
                    _socketState.value = SocketState.DISCONNECTED
                }


                connect()
            }

            Log.d(TAG, "Socket initialization complete")

        } catch (e: Exception) {
            Log.e(TAG, "Socket initialization failed", e)
            _socketState.value = SocketState.DISCONNECTED
            isInitialized = false
            socket = null
        }
    }

    fun isConnected(): Boolean {
        return socket?.connected() == true
    }

    fun emitOnline(userId: String) {
        try {
            if (!isConnected()) {
                Log.w(TAG, "Cannot emit online - socket not connected")
                return
            }

            if (userId.isEmpty()) {
                Log.w(TAG, "Cannot emit online - userId is empty")
                return
            }

            socket?.emit("login", userId)
            Log.d(TAG, "User online emitted: $userId")

        } catch (e: Exception) {
            Log.e(TAG, "Error emitting online event", e)
        }
    }

    fun emitOffline(userId: String) {
        try {
            if (!isConnected()) {
                Log.w(TAG, "Cannot emit offline - socket not connected")
                return
            }

            if (userId.isEmpty()) {
                Log.w(TAG, "Cannot emit offline - userId is empty")
                return
            }

            socket?.emit("logout", userId)
            Log.d(TAG, "User offline emitted: $userId")

        } catch (e: Exception) {
            Log.e(TAG, "Error emitting offline event", e)
        }
    }

    fun disconnect(userId: String) {
        try {
            Log.d(TAG, "Disconnecting socket for user: $userId")

            // Emit offline if connected
            if (isConnected() && userId.isNotEmpty()) {
                emitOffline(userId)
            }

            // Clean up socket
            cleanupSocket()

            _socketState.value = SocketState.DISCONNECTED
            isInitialized = false

            Log.d(TAG, "Socket disconnected successfully")

        } catch (e: Exception) {
            Log.e(TAG, "Error during socket disconnect", e)
        } finally {
            socket = null
        }
    }

    private fun cleanupSocket() {
        try {
            socket?.let {
                if (it.connected()) {
                    it.disconnect()
                }
                it.off() // Remove all event listeners
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning up socket", e)
        }
    }

    // Safe way to get socket - returns null if not available
    fun getSocketOrNull(): Socket? {
        return if (isInitialized && socket?.connected() == true) {
            socket
        } else {
            Log.w(TAG, "Socket not available - initialized: $isInitialized, connected: ${socket?.connected()}")
            null
        }
    }

    // Generic emit function for other events
    fun emit(event: String, vararg args: Any) {
        try {
            if (!isConnected()) {
                Log.w(TAG, "Cannot emit event '$event' - socket not connected")
                return
            }

            socket?.emit(event, *args)
            Log.d(TAG, "Event emitted: $event")

        } catch (e: Exception) {
            Log.e(TAG, "Error emitting event: $event", e)
        }
    }

    // Listen to custom events
    fun on(event: String, callback: (Array<Any>) -> Unit) {
        try {
            socket?.on(event) { args ->
                try {
                    callback(args)
                } catch (e: Exception) {
                    Log.e(TAG, "Error in callback for event: $event", e)
                }
            }
            Log.d(TAG, "Listener registered for event: $event")

        } catch (e: Exception) {
            Log.e(TAG, "Error registering listener for event: $event", e)
        }
    }

    // Remove listener for specific event
    fun off(event: String) {
        try {
            socket?.off(event)
            Log.d(TAG, "Listener removed for event: $event")
        } catch (e: Exception) {
            Log.e(TAG, "Error removing listener for event: $event", e)
        }
    }

    // Only use this if you absolutely need to throw an error
    @Deprecated(
        message = "Use getSocketOrNull() instead to avoid crashes",
        replaceWith = ReplaceWith("getSocketOrNull()"),
        level = DeprecationLevel.WARNING
    )
    fun getSocket(): Socket? {
        return try {
            socket?.takeIf { it.connected() }
                ?: throw IllegalStateException("Socket is not initialized or not connected. Call init() first.")
        } catch (e: Exception){
            null
        }
    }
}

enum class SocketState {
    CONNECTING,
    CONNECTED,
    DISCONNECTED
}