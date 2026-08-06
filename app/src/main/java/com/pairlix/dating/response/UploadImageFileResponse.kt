package com.pairlix.dating.response


import com.google.gson.annotations.SerializedName

data class UploadImageFileResponse(
    @SerializedName("data")
    var `data`: String? = "",
    @SerializedName("message")
    var message: String? = ""
)