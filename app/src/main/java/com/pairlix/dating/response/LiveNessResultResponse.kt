package com.pairlix.dating.response

import com.google.gson.annotations.SerializedName


data class LiveNessResultResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?) {

    data class Data(
        @SerializedName("confidence")
        var confidence: Double?,
        @SerializedName("isLive")
        var isLive: Boolean?,
        @SerializedName("status")
        var status: String?
    )
}

/*  @SerializedName("data")
  var `data`: Data? = Data(),
  @SerializedName("message")
  var message: String? = "",
  @SerializedName("status")
  var status: Int? = 0
) {
  data class Data(
      @SerializedName("confidence")
      var confidence: Double? = 0.0,
      @SerializedName("liveFaceImageKey")
      var liveFaceImageKey: Any? = Any(),
      @SerializedName("livenessStatus")
      var livenessStatus: String? = ""
  )
}*/


