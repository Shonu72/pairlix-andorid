package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class ProfileViewActionRequest(
    @SerializedName("isFullProfileView")
    var isFullProfileView: Boolean? = false,
    @SerializedName("toUserId")
    var toUserId: String? = "",
    @SerializedName("viewDuration")
    var viewDuration: Number? = 0
)


