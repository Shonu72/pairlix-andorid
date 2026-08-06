package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class CompleteProfileRequest6(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("step")
    val step: Int? = null
) {
    data class Data(
        @SerializedName("interests")
        val interests: List<Interest?>? = null
    ) {
        data class Interest(
            @SerializedName("categoryId")
            val categoryId: String? = null,
            @SerializedName("tagIds")
            val tagIds: List<String?>? = null
        )
    }
}


