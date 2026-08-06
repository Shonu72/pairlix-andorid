package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class MatchPopupResponse(
    @SerializedName("data")
    var `data`: List<Data?>? = listOf(),
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
) {
    data class Data(
        @SerializedName("firstName")
        var firstName: String? = "",
        @SerializedName("isMatch")
        var isMatch: Boolean? = false,
        @SerializedName("lastName")
        var lastName: String? = "",
        @SerializedName("matchedAt")
        var matchedAt: String? = "",
        @SerializedName("myAction")
        var myAction: Any? = Any(),
        @SerializedName("profileImages")
        var profileImages: List<String?>? = listOf(),
        @SerializedName("theirAction")
        var theirAction: String? = "",
        @SerializedName("userId")
        var userId: String? = ""
    )
}