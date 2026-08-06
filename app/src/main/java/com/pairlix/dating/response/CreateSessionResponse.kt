package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class CreateSessionResponse(
    @SerializedName("data")
    var `data`: Data? = Data(),
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("status")
    var status: Int? = 0
) {
    data class Data(
        @SerializedName("sessionId")
        var sessionId: String? = ""
    )
}


