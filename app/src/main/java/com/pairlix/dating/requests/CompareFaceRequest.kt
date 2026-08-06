package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class CompareFaceRequest(
    @SerializedName("documentImageKey")
    var documentImageKey: String? = "",
    @SerializedName("liveFaceImageKey")
    var liveFaceImageKey: String? = ""
)


