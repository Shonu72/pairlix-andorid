package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class ModerateContentRequest(
    @SerializedName("imageKey")
    var imageKey: List<String?>?=null,
    @SerializedName("text")
    var text: String?=null
)