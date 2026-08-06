package com.pairlix.dating.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.utils.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import org.json.JSONObject

@HiltViewModel
class SocketViewModel @Inject constructor(
    private val socketManager: SocketManager,
    @ApplicationContext context: Context
) : ViewModel() {
    fun getUserId(context: Context): String {
       return SharedPreference.get(context).userID
    }
    private val userId = getUserId(context) // get from datastore / auth

    val socketState = socketManager.socketState

    fun connectSocket(id: String) {
        socketManager.init(id)

    }

    fun sendOnline() {
        socketManager.emitOnline(userId)
    }

    fun sendOffline() {
        socketManager.disconnect(userId)
    }

    fun emitHomePageStatus(isOnHomePage: Boolean) {
        try {
            val data = JSONObject().apply {
                put("isUserOnHomePage", isOnHomePage)
            }
            socketManager.emit("homePageStatus", data)
            Log.d("SocketViewModel", "emitHomePageStatus → isUserOnHomePage: $isOnHomePage | data: $data")
        } catch (e: Exception) {
            Log.e("SocketViewModel", "emitHomePageStatus failed: ${e.message}", e)
        }
    }
}
