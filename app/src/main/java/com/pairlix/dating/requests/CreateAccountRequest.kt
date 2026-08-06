package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class CreateAccountRequest(

    @SerializedName("countryCode")
    val countryCode: String? = null,
    @SerializedName("deviceToken")
    val deviceToken: String? = null,
    @SerializedName("deviceType")
    val deviceType: String? = null,
    @SerializedName("dob")
    val dob: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("countryIso")
    val countryIso: String? = null,
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("cityAr")
    val cityAr: String? = null,
    @SerializedName("countryName")
    val countryName: String? = null,
    @SerializedName("countryNameAr")
    val countryNameAr: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    val longitude: String? = null,
    val latitude: String? = null,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
    @SerializedName("profileImages")
    val profileImages: String? = null,
    @SerializedName("uniqueId")
    val uniqueId: String? = null,
)


