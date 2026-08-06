package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class ActionRequest(
    @SerializedName("action")
    var action: String? = null,
    @SerializedName("customReason")
    var customReason: String? = null,
    @SerializedName("reportReason")
    var reportReason: String? = null,
    @SerializedName("toUserId")
    var toUserId: String? = null
)


