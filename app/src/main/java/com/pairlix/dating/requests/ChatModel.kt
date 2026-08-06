package com.pairlix.dating.requests

data class ChatMessage(
    val _id: String? = null,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val type: String ?= "",
    val duration: String?,
    val createdAt: String,
    val readAt: String? = null,
    val localId: String? = null,
    val isUploading: Boolean = false,
    val isFailed: Boolean = false,
    val read: Boolean = false,
    val isOnline: Boolean = false,
    val matchUpdatedAt: String? = null,



    )