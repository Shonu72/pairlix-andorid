package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class OtpVerifyRequest(
    @SerializedName("emailOtp")
    val emailOtp: Int? = null,
    @SerializedName("phoneOtp")
    val phoneOtp: Int? = null
)