package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class RecentSearchHistoryResponse(
    @SerializedName("data")
    val `data`: List<Any?>? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("success")
    val success: Boolean? = null
)


