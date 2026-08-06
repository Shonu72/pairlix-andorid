package com.pairlix.dating.requests

data class IncomingCallData(
    val roomId: String?=null,
    val callerId: String?=null,
    val callType: String?=null,
    val channelName: String?=null,
    val token: String?=null,
    val profileImages: String?=null,
    val firstName: String?=null,
    val lastName: String?=null,
    val age: String?=null,
    val uid: Int?=null
)

