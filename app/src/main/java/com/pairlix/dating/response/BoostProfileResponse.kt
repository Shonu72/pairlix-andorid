package com.pairlix.dating.response

import com.google.gson.annotations.SerializedName

data class BoostProfileResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("success")
    var success: Boolean?
) {

    data class Data(
        @SerializedName("boostEndTime")
        var boostEndTime: String?,
        @SerializedName("boostStartTime")
        var boostStartTime: String?,
        @SerializedName("isBoostActive")
        var isBoostActive: Boolean?
    )

}
