package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class UploadDocumentFileResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("isImageInvalid")
    var isImageInvalid: Boolean=false,

) {

    data class Data(
        @SerializedName("documentImageKey")
        var documentImageKey: String?,
        @SerializedName("documentImageUrl")
        var documentImageUrl: String?
    )
}