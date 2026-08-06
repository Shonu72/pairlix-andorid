package com.pairlix.dating.response

import com.google.gson.annotations.SerializedName

data class OtpResponse(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("success")
    var success: Boolean? = null
) {
    data class Data(
        @SerializedName("accesstoken")
        val accesstoken: String? = null,
        @SerializedName("user")
        val user: User? = null,
        @SerializedName("currentStep")
        val currentStep: Int? = null,

    ) {
        data class User(
            @SerializedName("accessToken")
            val accessToken: Any? = null,
            @SerializedName("age")
            val age: Int? = null,
            @SerializedName("ageSettings")
            val ageSettings: Boolean? = null,
            @SerializedName("blurMyProfile")
            val blurMyProfile: Boolean? = null,
            @SerializedName("city")
            val city: String? = null,
            @SerializedName("continousMatches")
            val continousMatches: Any? = null,
            @SerializedName("countryCode")
            val countryCode: String? = null,
            @SerializedName("countryIso")
            val countryIso: String? = null,
            @SerializedName("countryName")
            val countryName: String? = null,
            @SerializedName("createdAt")
            val createdAt: Long? = null,
            @SerializedName("deviceToken")
            val deviceToken: String? = null,
            @SerializedName("deviceType")
            val deviceType: String? = null,
            @SerializedName("dislikes")
            val dislikes: Any? = null,
            @SerializedName("distanceSettings")
            val distanceSettings: Boolean? = null,
            @SerializedName("dob")
            val dob: String? = null,
            @SerializedName("email")
            val email: String? = null,
            @SerializedName("ethnicity")
            val ethnicity: Any? = null,
            @SerializedName("firstName")
            val firstName: String? = null,
            @SerializedName("gender")
            val gender: String? = null,
            @SerializedName("hasABio")
            val hasABio: Boolean? = null,
            @SerializedName("_id")
            val id: String? = null,
            @SerializedName("isActive")
            val isActive: Boolean? = null,
            @SerializedName("isBlocked")
            val isBlocked: Boolean? = null,
            @SerializedName("isDeleted")
            val isDeleted: Boolean? = null,
            @SerializedName("isMailOtpVerified")
            val isMailOtpVerified: Boolean? = null,
            @SerializedName("isMatched")
            val isMatched: Boolean? = null,
            @SerializedName("isOnline")
            val isOnline: Boolean? = null,
            @SerializedName("isPhoneOtpVerified")
            val isPhoneOtpVerified: Boolean? = null,
            @SerializedName("isProfileCompleted")
            val isProfileCompleted: Boolean? = null,
            @SerializedName("isUserVerified")
            val isUserVerified: Int? = null,
            @SerializedName("language")
            val language: String? = null,
            @SerializedName("lastActive")
            val lastActive: Boolean? = null,
            @SerializedName("lastLogin")
            val lastLogin: Any? = null,
            @SerializedName("lastName")
            val lastName: String? = null,
            @SerializedName("likes")
            val likes: Any? = null,
            @SerializedName("locationPermission")
            val locationPermission: LocationPermission? = null,
            @SerializedName("locationSettings")
            val locationSettings: Boolean? = null,

            @SerializedName("notificationPermission")
            val notificationPermission: Boolean? = null,
            @SerializedName("notifyOnline")
            val notifyOnline: Boolean? = null,
            @SerializedName("personalDetails")
            val personalDetails: String? = null,
            @SerializedName("phoneNumber")
            val phoneNumber: String? = null,
            @SerializedName("privateAccount")
            val privateAccount: Boolean? = null,
            @SerializedName("profileImages")
            val profileImages: List<String?>? = null,
            @SerializedName("rejectionReason")
            val rejectionReason: String? = null,
            @SerializedName("profileStatus")
            val profileStatus: Int? = null,
            @SerializedName("seeFilter")
            val seeFilter: Int? = null,
            @SerializedName("messageFilter")
            val messageFilter: Int? = null,
            @SerializedName("superlikes")
            val superlikes: Any? = null,
            @SerializedName("updatedAt")
            val updatedAt: Long? = null,
            @SerializedName("userType")
            val userType: Any? = null
        ) {
            data class LocationPermission(
                @SerializedName("allow")
                val allow: Boolean? = null,
                @SerializedName("deny")
                val deny: Boolean? = null
            )


        }
    }
}