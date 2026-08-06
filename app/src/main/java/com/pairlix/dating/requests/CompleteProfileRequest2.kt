package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class CompleteProfileRequest2(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("step")
    val step: Int? = null
) {
    data class Data(
        @SerializedName("customSect")
        val customSect: String? = null,
        @SerializedName("interestedIn")
        val interestedIn: String? = null,
        @SerializedName("sect")
        val sect: String? = null,
        @SerializedName("spokenLanguages")
        val spokenLanguages: List<String?>? = null
    )
}