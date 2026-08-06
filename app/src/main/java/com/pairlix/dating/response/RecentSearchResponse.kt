package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class RecentSearchResponse(
    @SerializedName("data")
    val `data`: List<Data?>? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("success")
    var success: Boolean? = null
) {
    data class Data(
        @SerializedName("categoryId")
        val categoryId: String? = null,
        @SerializedName("iconImage")
        val iconImage: String? = null,
        @SerializedName("tagId")
        val tagId: String? = null,
        @SerializedName("tagNameAr")
        val tagNameAr: String? = null,
        @SerializedName("tagNameEn")
        val tagNameEn: String? = null
    )
}


