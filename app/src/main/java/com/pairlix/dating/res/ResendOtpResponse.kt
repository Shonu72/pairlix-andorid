package com.pairlix.dating.res
import com.google.gson.annotations.SerializedName
data class ResendOtpResponse(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("success")
    val success: Boolean? = null
) {
    data class Data(
        @SerializedName("accessToken")
        val accessToken: String? = null,
        @SerializedName("emailOtp")
        val emailOtp: Any? = null,
        @SerializedName("phoneOtp")
        val phoneOtp: Int? = null
    )
}