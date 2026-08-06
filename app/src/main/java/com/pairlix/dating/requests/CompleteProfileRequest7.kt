package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class CompleteProfileRequest7(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("step")
    val step: Int? = null
) {
    data class Data(
        @SerializedName("aboardAfterMarriage")
        val aboardAfterMarriage: String? = null,
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("faithIds")
        val faithIds: List<String?>? = null,
        @SerializedName("height")
        val height: String? = null,
        @SerializedName("heightType")
        val heightType: String? = null
    )


}