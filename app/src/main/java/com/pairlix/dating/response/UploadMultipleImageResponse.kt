package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class UploadMultipleImageResponse(
    @SerializedName("data")
    val `data`: List<String?>? = null,
    @SerializedName("message")
    val message: String? = null
)


