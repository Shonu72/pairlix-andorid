package com.pairlix.dating.response

import com.google.gson.annotations.SerializedName

data class MatchTimingResponse(

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: MatchTimingData?
)

data class MatchTimingData(

    @SerializedName("activityId")
    val activityId: String?,

    @SerializedName("fromUser")
    val fromUser: String?,

    @SerializedName("toUser")
    val toUser: String?,

    @SerializedName("isMatch")
    val isMatch: Boolean?,

    @SerializedName("updatedAt")
    val updatedAt: String?
)