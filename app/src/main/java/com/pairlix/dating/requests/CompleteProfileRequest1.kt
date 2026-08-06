package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class CompleteProfileRequest1(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("step")
    val step: Int? = null
) {
    data class Data(
        @SerializedName("images")
        val images: List<String?>? = null
    )
}


