package com.pairlix.dating.agora

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pairlix.dating.R
import com.pairlix.dating.requests.AgoraCallData
import com.pairlix.dating.requests.IncomingCallData
import com.pairlix.dating.utils.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update

@HiltViewModel
class CallViewModel @Inject constructor(
    private val socketManager: SocketManager
) : ViewModel()
{

    private var mediaPlayerCaller: MediaPlayer? = null

    fun playRingtone(context: Context) {
        stopRingtone() // ✅ always clean up before creating new one
        try {
            mediaPlayerCaller = MediaPlayer.create(context, R.raw.phone_ringing_tone)
            if (mediaPlayerCaller == null) {
                Log.e("CallManager", "❌ MediaPlayer.create() returned null — check R.raw.phone_receive_ringtone exists")
                return
            }
            mediaPlayerCaller?.apply {
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
            mediaPlayerCaller?.let {
                if (it.isPlaying) it.stop()
                it.release()
            }
        } catch (e: Exception) {
            Log.e("CallManager", "stopRingtone error: ${e.message}")
        } finally {
            mediaPlayerCaller = null
        }
    }
    private fun socket() = socketManager.getSocket()

    // 📥 Incoming call (receiver)
    private val _incomingCall = MutableStateFlow<IncomingCallData?>(null)
    val incomingCall: StateFlow<IncomingCallData?> = _incomingCall

    private val _callEnded = MutableStateFlow<Boolean?>(null)
    val callEnded: StateFlow<Boolean?> = _callEnded

    // 🔐 Agora credentials (both sides)
    private val _agoraData = MutableStateFlow<AgoraCallData?>(null)
    val agoraData: StateFlow<AgoraCallData?> = _agoraData

    // 📞 Call active state
    private val _isInCall = MutableStateFlow(false)
    val isInCall: StateFlow<Boolean> = _isInCall

    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token

    private val _channelName = MutableStateFlow("")
    val channelName: StateFlow<String> = _channelName

    private val _uid = MutableStateFlow(0)
    val uid: StateFlow<Int> = _uid


    private val _type = MutableStateFlow("")
    val type: StateFlow<String> = _type


    private val _socketError = MutableStateFlow<String?>(null)
    val socketError: StateFlow<String?> = _socketError

    fun clearError(){
        _socketError.value=""
    }

    private val _incomingRequest = MutableStateFlow(IncomingCallData())
    val incomingRequest: StateFlow<IncomingCallData> = _incomingRequest

    fun updateSenderData(name: String, lastName: String, age: String, image: String){
        _incomingRequest.update { current->
            var request=current
            request.copy(firstName =name,
                lastName = lastName,
                age = age,
                profileImages = image

            )

        }
    }

    fun resetState(){
        socket()?.off("callEnded")

        socket()?.off("callRejected")
    }

    private val _roomId = MutableStateFlow("")
    val roomId: StateFlow<String> = _roomId

    private val _callDuration = MutableStateFlow(0L)
    val callDuration: StateFlow<Long> = _callDuration

    private val _callStarted = MutableStateFlow(false)
    val callStarted: StateFlow<Boolean> = _callStarted

    private val _callType = MutableStateFlow("")
    val callType: StateFlow<String> = _callType

    private val _callerName = MutableStateFlow("")
    val callerName: StateFlow<String> = _callerName

    fun updateCallType(value: String){
        _callType.value=value
    }

    fun updateCallerName(value: String){
        _callerName.value=value
    }


    fun resetCallEnd(){
        _callEnded.value=null
    }


    private var timeJob: Job? = null


    fun setAgoraData(token: String,uid: Int,channelName: String,roomId: String){
        _token.value=token
        _uid.value=uid
        _channelName.value=channelName
        _roomId.value=roomId
    }

    fun setCallerType(type: String){
       _type.value=type
    }
       private val _acceptCall = MutableStateFlow(false)
    val acceptCall: StateFlow<Boolean> = _acceptCall



    // 🧠 Outgoing call type memory
    private val _lastCallType = MutableStateFlow<String?>(null)
    val lastCallType: StateFlow<String?> = _lastCallType

    // --------------------------------------------------
    // 🔊 SOCKET LISTENERS
    // --------------------------------------------------
    fun listenCallEvents() {
        val s = socket() ?: return
        s.off("incomingCall")
        s.off("callStarted")
        s.off("callAccepted")
        s.off("callRejected")
        s.off("callEnded")

        // 📥 RECEIVER: incoming call (NO TOKEN HERE)
        s.on("incomingCall") { args ->

            val data = args[0] as JSONObject

            val caller = data.getJSONObject("caller")

            val imagesArray = caller.getJSONArray("profileImages")
            val firstImage = if (imagesArray.length() > 0) imagesArray.getString(0) else ""

            _incomingCall.value = IncomingCallData(
                roomId = data.getString("roomId"),
                callType = data.getString("callType"),
                channelName = data.getString("channelName"),
                token = data.getString("token"),
                uid = data.getInt("uid"),
                // ✅ Caller details
                firstName = caller.getString("firstName"),
                lastName = caller.getString("lastName"),
                profileImages = firstImage,
                age = caller.getInt("age").toString()
            )


        }

        s.off("error")

        s.on("error") { args ->
            if (args.isEmpty()) return@on
            val data = args[0] as JSONObject
            val code = data.optString("code", "")
            val message = data.optString("message", "")

            if (
                message.isNotEmpty()
            ) {
                _callStarted.value=false
                _socketError.value = message
            }
        }

        s.on("callMissed") { args ->

            val data = args[0] as JSONObject
            AgoraManager.leaveCall()
            AgoraManager.destroy()
            // Reset states
            resetCall()
            _callEnded.value=true
            resetState()
            Log.e("SOCKET_CALLER2", "📤 ${_callEnded.value}:")
        }


        // 📤 CALLER: Agora details
        s.on("callStarted") { args ->


            val data = args[0] as JSONObject

            val channelName = data.getString("channelName").trim()

            val token = data.getString("token").trim()

            val uid = data.getInt("uid")

            Log.e("SOCKET_CALLER", "📤 callStarted received:")

            Log.e("SOCKET_CALLER", "  channelName: '$channelName'")

            Log.e("SOCKET_CALLER", "  uid: $uid")

            Log.e("SOCKET_CALLER", "  token: ${token.take(50)}...")

            Log.e("SOCKET_CALLER", "  token length: ${token.length}")

            _agoraData.value = AgoraCallData(

                channelName = channelName,

                token = token,

                uid = uid

            )

            _isInCall.value = true

        }

        s.on("callCancelled") { args ->

            val data = args[0] as JSONObject
            val roomId = data.optString("roomId")

            Log.e("SOCKET", "❌ callCancelled received for room: $roomId")

            // 🔊 Stop ringtone
            stopRingtone()

            // 🛑 Reset call state
            resetCall()

            // 🔚 Close UI
            _callEnded.value = true

            // 🧹 Cleanup Agora (safe)
            AgoraManager.leaveCall()
            AgoraManager.destroy()
        }

        s.on("callAccepted") { args ->
            _callStarted.value=false

            val data = args[0] as JSONObject

            if (data.has("token") && data.has("uid")) {

                val channelName = data.getString("channelName").trim()

                val token = data.getString("token").trim()

                val uid = data.getInt("uid")

                Log.e("SOCKET_RECEIVER", "📥 callAccepted received:")

                Log.e("SOCKET_RECEIVER", "  channelName: '$channelName'")

                Log.e("SOCKET_RECEIVER", "  uid: $uid")

                Log.e("SOCKET_RECEIVER", "  token: ${token.take(50)}...")

                Log.e("SOCKET_RECEIVER", "  token length: ${token.length}")

                _agoraData.value = AgoraCallData(

                    channelName = channelName,

                    token = token,

                    uid = uid

                )

                _acceptCall.value = true

            } else {

                _acceptCall.value = true

            }

            _isInCall.value = true
            _incomingCall.value = null

        }




        // ❌ Call Rejected by Receiver
        s.on("callRejected") { args ->
            _callStarted.value=false

            Log.e("SOCKET", "❌ Call Rejected Received")

            // Stop timer
            stopTimer()

            // Leave Agora channel safely
            AgoraManager.leaveCall()
            AgoraManager.destroy()

            // Reset states
            resetCall()
            _callEnded.value=true
            Log.e("SOCKET_CALLER3", "📤 ${_callEnded.value}:")

        }


        socket()?.on("callEnded") { args ->

            val data = args[0] as JSONObject
            val duration = data.getLong("duration")

            Log.e("SOCKET", "❌ Call Ended Received")

            _callEnded.value=true// stop timer loop
            Log.e("SOCKET_CALLER1", "📤 ${_callEnded.value}:")

            stopTimer()
            resetState()

            AgoraManager.leaveCall()
            AgoraManager.destroy()


            // save final duration

        }

    }



    fun endCall(){
        _callDuration.value=0L

        resetCall()
    }

    fun callEnd()
    {
        _callEnded.value=null
    }
    // --------------------------------------------------
    // 📤 EMITS
    // --------------------------------------------------
    fun startCall(roomId: String, receiverId: String, callType: String) {
        _lastCallType.value = callType
        _callStarted.value=true
        socket()?.emit(
            "startCall",
            JSONObject().apply {
                put("roomId", roomId)
                put("receiverId", receiverId)
                put("callType", callType)
            }
        )
    }

    fun acceptCall(roomId: String) {
        socket()?.emit(
            "acceptCall",
            JSONObject().put("roomId", roomId)
        )
    }

    fun rejectCall(roomId: String) {
        socket()?.emit("rejectCall", JSONObject().put("roomId", roomId))
        resetCall()
    }




    fun endCall(roomId: String) {
        try {
            val obj = JSONObject().apply {
                put("roomId", roomId)
            }
            socket()?.emit("endCall", obj)

            Log.e("SOCKET", "📞 endCall emitted for room: $roomId")

        } catch (e: Exception) {
            Log.e("SOCKET", "❌ endCall error: ${e.message}")
        }
    }

    fun cancelCall(roomId: String) {
        socket()?.emit("cancelCall", JSONObject().put("roomId", roomId))
        resetCall()
    }
    // --------------------------------------------------
    // 🔄 RESET
    // --------------------------------------------------
     fun resetCall() {
        _incomingCall.value = null
        _agoraData.value = null
        _isInCall.value = false
        _lastCallType.value = null
        _callDuration.value = 0L
    }


    override fun onCleared() {
        socket()?.off()
        super.onCleared()
    }

    fun startTimer() {
        viewModelScope.launch {
            while (isInCall.value) {
                delay(1000)
                _callDuration.value++
            }
        }
    }

    fun stopTimer() {
        _isInCall.value = false
    }

}