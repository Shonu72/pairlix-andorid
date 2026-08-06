package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class PurchasedPlanRequest(
    @SerializedName("countryName")
    val countryName: String? = null,
    @SerializedName("duration")
    val duration: String? = null,
    @SerializedName("expiredOn")
    val expiredOn: String? = null,
    @SerializedName("planType")
    val planType: Int? = null,
    @SerializedName("price")
    val price: String? = null,
    @SerializedName("purchasedOn")
    val purchasedOn: String? = null,
    @SerializedName("paymentStatus")
    val paymentStatus:Int?=0
)


