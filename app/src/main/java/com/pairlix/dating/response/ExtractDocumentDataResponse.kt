package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class ExtractDocumentDataResponse(
    @SerializedName("data")
    var `data`: Data? = Data(),
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
) {
    data class Data(
        @SerializedName("extracted")
        var extracted: Extracted? = Extracted(),
        @SerializedName("upload_user_file")
        var uploadUserFile: String? = null
    ) {
        data class Extracted(
            @SerializedName("dob")
            var dob: String?=null,
            @SerializedName("dobType")
            var dobType: Any? = Any()
        )
    }
}


