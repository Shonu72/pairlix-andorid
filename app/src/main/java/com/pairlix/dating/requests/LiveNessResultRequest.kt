package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class LiveNessResultRequest(
    @SerializedName("sessionId")
    var sessionId: String? = ""
)


