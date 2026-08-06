package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class GetAllFaithsStep7Response(
    @SerializedName("data")
    val `data`: List<Data?>? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("success")
    var success: Boolean? = null
) {
    data class Data(
        @SerializedName("faithNameAr")
        val faithNameAr: String? = null,
        @SerializedName("faithNameEn")
        val faithNameEn: String? = null,
        @SerializedName("iconImage")
        val iconImage: String? = null,
        @SerializedName("_id")
        val id: String? = null
    )
}


