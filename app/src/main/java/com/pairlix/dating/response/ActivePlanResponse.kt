package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class ActivePlanResponse(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("success")
    var success: Boolean? = null
) {
    data class Data(
        @SerializedName("countryName")
        val countryName: String? = null,
        @SerializedName("createdAt")
        val createdAt: String? = null,
        @SerializedName("duration")
        val duration: String? = null,
        @SerializedName("expiredOn")
        val expiredOn: String? = null,
        @SerializedName("_id")
        val id: String? = null,
        @SerializedName("isAutoRenewal")
        val isAutoRenewal: Boolean? = null,
        @SerializedName("planType")
        val planType: Int? = null,
        @SerializedName("activePlanType")
        val activePlanType: Int? = null,
        @SerializedName("price")
        val price: Int? = null,
        @SerializedName("purchasedOn")
        val purchasedOn: String? = null,
        @SerializedName("status")
        val status: String? = null,
        @SerializedName("updatedAt")
        val updatedAt: String? = null,
        @SerializedName("userId")
        val userId: String? = null
    )
}


