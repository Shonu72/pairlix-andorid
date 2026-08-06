package com.pairlix.dating.requests

data class MatchNotificationData(
    val senderName: String,
    val senderImage: String,
    val senderAge: String,
    val senderOnline: Boolean,
    val senderFaceVerified: Boolean,
    val senderDocumentVerified: Boolean,

    val receiverName: String,
    val receiverImage: String,
    val receiverAge: String,
    val receiverOnline: Boolean,
    val receiverFaceVerified: Boolean,
    val receiverDocumentVerified: Boolean,

    val matchId: String,
    val matchedOn: String
)