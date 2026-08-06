package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class LoginResponse(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("success")
    var success: Boolean? = null
) {
    data class Data(
        @SerializedName("accessToken")
        val accessToken: String? = null,
        @SerializedName("otp")
        val otp: String? = null
    )
}


