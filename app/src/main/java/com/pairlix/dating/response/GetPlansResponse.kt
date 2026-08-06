package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class GetPlansResponse(
    @SerializedName("data")
    val `data`: List<Data?>? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("success")
    var success: Boolean? = null
) {
    data class Data(
        @SerializedName("badge")
        val badge: String? = null,
        @SerializedName("countryPrices")
        val countryPrices: List<CountryPrice?>? = null,
        @SerializedName("createdAt")
        val createdAt: String? = null,
        @SerializedName("features")
        val features: List<String?>? = null,
        @SerializedName("_id")
        val id: String? = null,
        @SerializedName("isStatic")
        val isStatic: Boolean? = null,
        @SerializedName("planName")
        val planName: String? = null,
        @SerializedName("planType")
        val planType: Int? = null,
        @SerializedName("shortDescription")
        val shortDescription: String? = null,
        @SerializedName("updatedAt")
        val updatedAt: String? = null
    ) {
        data class CountryPrice(
            @SerializedName("countryName")
            val countryName: String? = null,
            @SerializedName("currency")
            val currency: String? = null,
            @SerializedName("_id")
            val id: String? = null,
            @SerializedName("prices")
            val prices: List<Price?>? = null
        ) {
            data class Price(
                @SerializedName("_id")
                val id: String? = null,
                @SerializedName("months")
                val months: Int? = null,
                @SerializedName("price")
                val price: Int? = null
            )
        }
    }
}