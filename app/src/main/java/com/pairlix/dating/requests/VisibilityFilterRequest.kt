package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class VisibilityFilterRequest(
    @SerializedName("ageSetting")
    var ageSetting: Boolean? = false,
    @SerializedName("blurProfile")
    var blurProfile: Boolean? = false,
    @SerializedName("distanceSetting")
    var distanceSetting: Boolean? = false,
    @SerializedName("isActive")
    var isActive: Boolean? = false,
    @SerializedName("locationSetting")
    var locationSetting: Boolean? = false,
    @SerializedName("messageFilter")
    var messageFilter: Int? = 0,
    @SerializedName("profileStatus")
    var profileStatus: Int? = 0,
    @SerializedName("seeFilter")
    var seeFilter: Int? = 0
)


