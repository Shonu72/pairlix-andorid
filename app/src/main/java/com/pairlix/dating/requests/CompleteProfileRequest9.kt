package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class CompleteProfileRequest9(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("step")
    val step: Int? = null
) {
    data class Data(
        @SerializedName("extraData")
        val extraData: String? = null,
        @SerializedName("uploadIdBack")
        val uploadIdBack: String? = null,
        @SerializedName("uploadIdFront")
        val uploadIdFront: String? = null,
        @SerializedName("documentDob")
        val documentDob: String? = null,
    )
}