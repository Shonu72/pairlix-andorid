package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class BoostProfileRequest(
    @SerializedName("isBoostActive")
    var isBoostActive: Boolean?
)


