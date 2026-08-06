package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class ActionResponse(
    @SerializedName("data")
    var `data`: Data? = Data(),
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
) {
    data class Data(
        @SerializedName("isMatch")
        var isMatch: Boolean? = false
    )
}