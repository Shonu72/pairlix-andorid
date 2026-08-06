package com.pairlix.dating.response

import com.google.gson.annotations.SerializedName

data class VisibilityFilterResponse(
    @SerializedName("data")
    var `data`: Data? = Data(),
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
)

class Data()


