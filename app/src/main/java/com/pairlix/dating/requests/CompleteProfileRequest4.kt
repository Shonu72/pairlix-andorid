package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class CompleteProfileRequest4(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("step")
    val step: Int? = null
) {
    data class Data(
        @SerializedName("haveChildren")
        val haveChildren: String? = null,
        @SerializedName("maritalStatus")
        val maritalStatus: String? = null,
        @SerializedName("religionPractice")
        val religionPractice: String? = null
    )
}