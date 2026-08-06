package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class CompleteProfileRequest3(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("step")
    val step: Int? = null
) {
    data class Data(
        @SerializedName("companyName")
        val companyName: String? = null,
        @SerializedName("currentProfession")
        val currentProfession: String? = null,
        @SerializedName("customProfession")
        val customProfession: String? = null,
        @SerializedName("educationLevel")
        val educationLevel: String? = null,
        @SerializedName("jobTitle")
        val jobTitle: String? = null,
        @SerializedName("schoolName")
        val schoolName: String? = null
    )
}


