package com.pairlix.dating.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.requests.ChatMessage
import com.pairlix.dating.response.GetChatListResponse
import com.pairlix.dating.response.Item
import com.pairlix.dating.utils.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val socketManager: SocketManager
) : ViewModel() {


    val socketState = socketManager.socketState
    // 🔥 Lazy socket access (NO crash)
    private fun socket() = socketManager.getSocket()

    var currentPage =   mutableStateOf(1)
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _roomId = MutableStateFlow<String?>(null)
    val roomId: StateFlow<String?> = _roomId


    private val _chatList = MutableStateFlow<List<Item>>(emptyList())
    val chatList: StateFlow<List<Item>> = _chatList

    private val _chatListLoading = MutableStateFlow(false)
    val chatListLoading: StateFlow<Boolean> = _chatListLoading

    private val _isOtherUserTyping = MutableStateFlow(false)
    val isOtherUserTyping: StateFlow<Boolean> = _isOtherUserTyping


    private val _isOtherUserRecording = MutableStateFlow(false)
    val isOtherUserRecording: StateFlow<Boolean> = _isOtherUserRecording

    private val _socketError = MutableStateFlow<String?>(null)
    val socketError: StateFlow<String?> = _socketError

    private val _isMessagesLoading = MutableStateFlow(true)
    val isMessagesLoading: StateFlow<Boolean> = _isMessagesLoading

    fun clearSocketError() {
        _socketError.value = null
    }

    fun emitCallError(message: String) {
        _socketError.value = message
    }


    private var typingJob: Job? = null
    private var currentTypingRoomId: String = ""
    private var recordingJob: Job? = null
    private var currentRecordingRoomId: String = ""

    fun joinRoom(toUserId: String) {
        val s = socket() ?: return
        // 1️⃣ listener FIRST
        s.off("roomJoined")
        s.on("roomJoined") { args ->

            if (args.isEmpty()) {
                println("roomJoined args empty")
                return@on
            }

            val data = args[0] as JSONObject
            _roomId.value = data.getString("roomId")
            println("roomJoined received roomId=${_roomId.value}")
        }
        // 2️⃣ emit AFTER listener
        s.emit(
            "joinRoom",
            JSONObject().apply {
                put("toUserId", toUserId)
            }
        )
    }
    fun clearList(){
        _chatList.value=emptyList()
    }

    fun setupChatListListener() {
        val s = socket() ?: return
        s.off("chatListResponse")
        s.on("chatListResponse") { args ->
            try {
                if (args.isEmpty()) return@on
                val response = args[0] as JSONObject
                Log.e("response", "${response} ", )
                val success = response.optBoolean("success", false)
                if (success) {
                    val data = response.getJSONObject("data")
                    val list = data.getJSONArray("list")
                    val chatItems = mutableListOf<Item>()

                    for (i in 0 until list.length()) {
                        val item = list.getJSONObject(i)
                        val otherUser = item.getJSONObject("otherUser")
                        val personalDetails = otherUser.optJSONObject("personalDetails")
                        val activePlanType = otherUser.optInt("activePlanType", 1)
                        val isFaceVerified = personalDetails?.optBoolean("isFaceVerified", false) ?: false
                        val isDocumentVerified = personalDetails?.optBoolean("isDocumentVerified", false) ?: false
                        chatItems.add(

                            Item(

                                roomId = item.optString("roomId", ""),
                                matchUpdatedAt = item.optString("matchUpdatedAt", ""),
                                lastMessage = item.optString("lastMessage", ""),
                                lastMessageAt = item.optString("lastMessageAt", ""),
                                lastMessageType = item.optString("lastMessageType", "text"),

                                lastMessageSenderId = item.optString("lastMessageSenderId", ""),
                                lastMessageReceiverId = item.optString("lastMessageReceiverId", ""),
                                readAt = item.optString("readAt", "")
                                    .takeIf { it.isNotBlank() && it != "null" },
                                otherUserId = otherUser.optString("_id", ""),
                                otherIsOnline = otherUser.optBoolean("isOnline",),
                                isActive = otherUser.optBoolean("isActive",),
                                otherUserFirstName = otherUser.optString("firstName", ""),
                                otherUserLastName = otherUser.optString("lastName", ""),
                                otherUserProfileImage = otherUser.optJSONArray("profileImages")?.optString(0) ?: "",
                                otherUserAge = otherUser.optInt("age", 0).toString(),
                                otherUserFaceVerified = isFaceVerified,
                                otherUserDocumentVerified = isDocumentVerified,
                                activePlanType = activePlanType,

                            )
                        )
                    }


                    _chatList.value = mergeAndSort(_chatList.value, chatItems)

                    Log.e("_chatList", "${_chatList.value} ", )

                    Log.d("ChatViewModel", "Total Chats: ${_chatList.value.size}")
                }

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error parsing chatListResponse", e)
            } finally {
                _chatListLoading.value = false
            }
        }


    }

    fun mergeAndSort(existing: List<Item>, incoming: List<Item>): List<Item> {
        val merged = (incoming + existing)          // incoming takes priority
            .distinctBy { it.roomId }               // remove duplicates
            .sortedByDescending { it.lastMessageAt } // keep sorted
        return merged
    }





    fun setupMessageReadListener(myUserId: String) {
        val s = socket() ?: return
        s.off("messageRead")
        s.on("messageRead") { args ->
            if (args.isEmpty()) return@on
            val data = args[0] as JSONObject
            val messageId = data.optString("messageId", "")
            val roomId = data.optString("roomId", "")
            val readAtTime = java.time.Instant.now().toString()

            // Update open chat screen messages
            _messages.update { list ->
                list.map { msg ->
                    if (msg._id == messageId) msg.copy(readAt = readAtTime) else msg
                }
            }

            // ✅ Update chatList double tick for the matching room
            _chatList.update { list ->
                list.map { item ->
                    if (item.roomId == roomId && item.lastMessageSenderId == myUserId) {
                        item.copy(readAt = readAtTime)
                    } else item
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun listenMessages(userId: String) {
        val s = socket() ?: return
        // 🔥 Remove duplicate listeners
        s.off("newMessage")
        s.off("chatHistory")
        s.off("messageRead")
        s.off("chatCleared")
        s.off("chatListResponse")
        s.off("userTyping")
        s.off("recordingStatus")
        s.off("messageDeleted")


        s.on("newMessage") { args ->
            val data = args[0] as JSONObject
            val msg = ChatMessage(
                _id = data.getString("_id"),
                senderId = data.getString("senderId"),
                receiverId = data.getString("receiverId"),
                message = data.getString("message"),
                type = data.getString("type"),
                duration = data.getString("duration"),
                createdAt = data.getString("createdAt"),
                readAt = data.optString("readAt")
            )
            if (msg.senderId == userId || msg.receiverId == userId)
                _messages.update { it + msg }


            val otherUserId = if (msg.senderId == userId) msg.receiverId else msg.senderId
            _chatList.update { list ->
                val updated = list.map { item ->
                    if (item.otherUserId == otherUserId) {
                        item.copy(
                            lastMessage = msg.message,
                            lastMessageAt = msg.createdAt,
                            lastMessageSenderId = msg.senderId,
                            readAt = null  // new message = unread
                        )
                    } else item
                }
                // re-sort so latest message goes to top
                updated.sortedByDescending { it.lastMessageAt }
            }


        }


        s.on("chatCleared") { args ->
            val data = args[0] as JSONObject
            _messages.value = emptyList()
        }


       /* s.on("messageRead") { args ->
            val data = args[0] as JSONObject
            val messageId = data.getString("messageId")
            val readAtTime = Instant.now().toString()
            _messages.update { list ->
                list.map { msg ->
                    if (msg._id == messageId) {
                        msg.copy(readAt = Instant.now().toString())
                    } else msg
                }
            }

        }*/


        // In listenMessages(), change the messageRead listener to only update _messages:
        s.on("messageRead") { args ->
            val data = args[0] as JSONObject
            val messageId = data.getString("messageId")
            val roomIdFromEvent = data.optString("roomId", "")
            val readAtTime = java.time.Instant.now().toString()

            _messages.update { list ->
                list.map { msg ->
                    if (msg._id == messageId) msg.copy(readAt = readAtTime) else msg
                }
            }

            // ✅ Keep chatList in sync from within chat screen too
            _chatList.update { list ->
                list.map { item ->
                    if (item.roomId == roomIdFromEvent) {
                        item.copy(readAt = readAtTime)
                    } else item
                }
            }
        }

        s.on("chatHistory") { args ->
            if (args.isEmpty()){
                _isMessagesLoading.value = false
                return@on
            }
            val root = args[0] as JSONObject
            val arr = root.getJSONArray("messages")
            val list = mutableListOf<ChatMessage>()



            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val message = obj.getString("message")
                val lower = message.lowercase()

                val type = when {
                    lower.endsWith(".m4a") ||
                            lower.endsWith(".mp3") ||
                            lower.endsWith(".aac") ||
                            lower.endsWith(".wav") -> "audio"

                    lower.endsWith(".mp4") ||
                            lower.endsWith(".avi") ||
                            lower.endsWith(".mkv") ||
                            lower.endsWith(".mov") ||
                            lower.endsWith(".wmv") ||
                            lower.endsWith(".flv") ||
                            lower.endsWith(".webm") ||
                            lower.endsWith(".m4v") ||
                            lower.endsWith(".3gp") -> "media"

                    lower.endsWith(".jpg") ||
                            lower.endsWith(".jpeg") ||
                            lower.endsWith(".png") ||
                            lower.endsWith(".webp") -> "media"

                    else -> "text"
                }

                list.add(
                    ChatMessage(
                        _id = obj.getString("_id"),
                        senderId = obj.getString("senderId"),
                        receiverId = obj.getString("receiverId"),
                        message = obj.getString("message"),
                        type = type,
                        duration = obj.optString("duration", "0"),
                        createdAt = obj.getString("createdAt"),
                        readAt = obj.optString("readAt", null),

                        )
                )
            }

            _messages.value = list
            _isMessagesLoading.value = false
        }
        // ✅ Listen: Typing
        s.on("userTyping") { args ->
            if (args.isEmpty()) return@on
            val data = args[0] as JSONObject
            val isTyping = data.optBoolean("isTyping", false)
            _isOtherUserTyping.value = isTyping
        }

        // ✅ Listen: Recording
        s.on("recordingStatus") { args ->
            if (args.isEmpty()) return@on
            val data = args[0] as JSONObject
            val isRecording = data.optBoolean("isRecording", false)
            _isOtherUserRecording.value = isRecording
        }

        // ✅ Listen: Message Deleted
        s.on("messageDeleted") {
            args ->
            if (args.isEmpty()) return@on
            val data = args[0] as JSONObject
            val messageId = data.optString("messageId", "")
            if (messageId.isNotEmpty()) {
                _messages.update { list ->
                    list.filter { it._id != messageId }
                }
            }


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
                _socketError.value = message
            }
        }

    }



    fun sendMessage(roomId: String, toUserId: String, text: String, type: String, clientId: String? = null ,duration: String) {
        val s = socket() ?: return

        s.emit("sendMessage", JSONObject().apply {
            put("roomId", roomId)
            put("toUserId", toUserId)
            put("message", text)
            put("type", type)
            put("duration", duration)
            clientId?.let { put("clientId", it) }   // ⭐ IMPORTANT

        })
    }

    fun loadMessages(roomId: String) {
        _isMessagesLoading.value = true
        val s = socket() ?: return
        s.emit("getMessages", JSONObject().apply {
            put("roomId", roomId)
            put("page", 1)
            put("limit", 50)
        })
    }

    fun readMessage(roomId: String,messageId: String) {
        val s = socket() ?: return

        s.emit("readMessage", JSONObject().apply {
            put("roomId", roomId)
            put("messageId", messageId)

        })
    }

    fun clearChat(roomId: String) {
        val s = socket() ?: return

        s.emit("clearChat", JSONObject().apply {
            put("roomId", roomId)


        })
    }

    fun requestChatList(
        search: String,
        page: Int,
        size: Int
    ) {
        val s = socket() ?: return

        val data = JSONObject().apply {
            put("search", search)
            put("page", page)
            put("size", size)
        }

        s.emit("getChatList", data)
        Log.e("daaaaaaaaaaaa", "$data: ", )
    }


  /*  fun stopRecording(roomId: String) {
        socket()?.emit("stopRecording", JSONObject().apply {
            put("roomId", roomId)
        })
    }*/

    fun stopRecording(roomId: String) {
        recordingJob?.cancel()
        recordingJob = null
        currentRecordingRoomId = ""

        socket()?.emit("stopRecording", JSONObject().apply {
            put("roomId", roomId)
        })
    }


    fun deleteMessage(roomId: String, messageId: String) {
        socket()?.emit("deleteMessage", JSONObject().apply {
            put("roomId", roomId)
            put("messageId", messageId)
        })
    }

   /* fun stopTyping(roomId: String) {
        socket()?.emit("stopTyping", JSONObject().apply {
            put("roomId", roomId)
        })
    }*/

   fun stopTyping(roomId: String) {
       typingJob?.cancel()
       typingJob = null
       currentTypingRoomId = ""
       socket()?.emit("stopTyping", JSONObject().apply {
           put("roomId", roomId)
       })
   }

    // ✅ Emit: Start Recording
 /*   fun startRecording(roomId: String) {
        socket()?.emit("startRecording", JSONObject().apply {
            put("roomId", roomId)
        })
    }*/

    fun startRecording(roomId: String) {

        if (currentRecordingRoomId == roomId && recordingJob?.isActive == true)
            return // Already recording in same room

        currentRecordingRoomId = roomId
        recordingJob?.cancel()

        recordingJob = viewModelScope.launch {
            while (isActive) {

                socket()?.emit("startRecording", JSONObject().apply {
                    put("roomId", roomId)
                })

                delay(500L) // 🔥 Repeat every 500ms (same as typing)
            }
        }
    }



    fun startTyping(roomId: String) {
        if (currentTypingRoomId == roomId && typingJob?.isActive == true) return // Already typing in same room

        currentTypingRoomId = roomId
        typingJob?.cancel()
        typingJob = viewModelScope.launch {
            while (isActive) {
                socket()?.emit("typing", JSONObject().apply {
                    put("roomId", roomId)
                })
                delay(500L) // Re-emit every 3 seconds
            }
        }
    }

    /*// ✅ Emit: Start Typing
    fun startTyping(roomId: String) {
        socket()?.emit("typing", JSONObject().apply {
            put("roomId", roomId)
        })
    }*/



    override fun onCleared() {
        super.onCleared()
        typingJob?.cancel()
    }

  /*  override fun onCleared() {
        super.onCleared()
        // Optional: remove listeners if needed
        socket()?.off()
    }*/


    fun leaveChat(roomId: String) {
        val s = socket() ?: return
        s.emit("leaveChat", JSONObject().apply {
            put("roomId", roomId)
        })
    }


}
