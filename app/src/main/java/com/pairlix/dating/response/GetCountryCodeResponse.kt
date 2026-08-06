package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class GetCountryCodeResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    var success: Boolean?
) {
    data class Data(
        @SerializedName("cities")
        val cities: List<City?>?,
        @SerializedName("limit")
        val limit: Int?,
        @SerializedName("page")
        val page: Int?,
        @SerializedName("total")
        val total: Int?,
        @SerializedName("totalPages")
        val totalPages: Int?,
        @SerializedName("translationPending")
        val translationPending: Boolean?,


    ) {
        data class City(
            @SerializedName("countryCode")
            val countryCode: String?,
            @SerializedName("latitude")
            val latitude: String?,
            @SerializedName("longitude")
            val longitude: String?,
            @SerializedName("nameEn")
            val nameEn: String?,
            @SerializedName("nameAr")
            val nameAr : String?,
            @SerializedName("stateCode")
            val stateCode: String?
        )
    }
}