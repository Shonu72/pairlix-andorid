package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class GetChatListResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("success")
    var success: Boolean?
) {

    data class Data(
        @SerializedName("list")
        var list: List<Item?>?,
        @SerializedName("pagination")
        var pagination: Pagination?
    )
}

    data class Item(
        @SerializedName("lastMessage")
        var lastMessage: String?,
        @SerializedName("lastMessageAt")
        var lastMessageAt: String?,
        @SerializedName("lastMessageSenderId")
        var lastMessageSenderId: String?,
        @SerializedName("lastMessageType")
        var lastMessageType: String?,
        @SerializedName("lastMessageReceiverId")
        var lastMessageReceiverId: String?,
        @SerializedName("readAt")
        val readAt: String? = null,
        @SerializedName("roomId")
        var roomId: String? = null,
        @SerializedName("otherUser")
        var otherUser: OtherUser? = null,
        @SerializedName("matchUpdatedAt")
        var matchUpdatedAt: String? = null,

        // ✅ Computed fields (not from JSON directly)
        var otherUserId: String? = null,
        var otherUserReadAt: String? = null,
        var otherUserFirstName: String? = null,
        var otherUserLastName: String? = null,
        var otherUserProfileImage: String? = null,
        var otherUserAge: String? = null,
        var otherIsOnline: Boolean? = null,
        var isActive: Boolean? = null,
        var otherUserFaceVerified: Boolean? = null,
        var otherUserDocumentVerified: Boolean? = null,
        var activePlanType: Int? = null

    )

    data class Pagination(
        @SerializedName("page")
        var page: Int?,
        @SerializedName("size")
        var size: Int?,
        @SerializedName("totalData")
        var totalData: Int?,
        @SerializedName("totalPages")
        var totalPages: Int?
    )

    data class OtherUser(
        @SerializedName("_id")
        val id: String,
        @SerializedName("firstName")
        val firstName: String,
        @SerializedName("lastName")
        val lastName: String,
        @SerializedName("age")
        val age: Int,
        @SerializedName("isOnline")
        val isOnline: Boolean = false,
        @SerializedName("profileImages")
        val profileImages: List<String>
    )


