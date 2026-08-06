package com.pairlix.dating.requests

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("countryCode")
    val countryCode: String? = null,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
    @SerializedName("deviceToken")
    val deviceToken: String? = null,
    val longitude: String? = null,
    val latitude: String? = null,
)


