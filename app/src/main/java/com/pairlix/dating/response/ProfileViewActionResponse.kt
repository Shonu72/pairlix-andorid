package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class ProfileViewActionResponse(
    @SerializedName("data")
    var `data`: Data? = Data(),
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
) {
    data class Data(
        @SerializedName("createdAt")
        var createdAt: String? = "",
        @SerializedName("fromUser")
        var fromUser: String? = "",
        @SerializedName("_id")
        var id: String? = "",
        @SerializedName("isFullProfileView")
        var isFullProfileView: Boolean? = false,
        @SerializedName("toUser")
        var toUser: String? = "",
        @SerializedName("updatedAt")
        var updatedAt: String? = ""
    )
}


