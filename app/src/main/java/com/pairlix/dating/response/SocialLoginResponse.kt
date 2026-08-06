package com.pairlix.dating.response

import com.google.gson.annotations.SerializedName

data class SocialLoginResponse(
    var `data`: Data? = null,
    var message: String? = null,
    var success: Boolean? = null
){

data class Data(
    var accesstoken: String? = null,
    var user: User? = null,
    @SerializedName("currentStep")
    val currentStep: Int? = null,
    @SerializedName("isProfileCompleted")
    val isProfileCompleted: Boolean? = null,
    @SerializedName("authType")
    val authType: String? = null,
)

data class User(
    var _id: String? = null,
    var accessToken: String? = null,
    var activePlanType: Int? = null,
    var age: Int? = null,
    var ageSetting: Boolean? = null,
    var blurProfile: Boolean? = null,
    var boostEndTime: Any? = null,
    var boostStartTime: Any? = null,
    var city: Any? = null,
    var countryCode: String? = null,
    var countryIso: Any? = null,
    var countryName: Any? = null,
    var createdAt: Long? = null,
    var dailyActivityScore: Int? = null,
    var day1ReminderSent: Boolean? = null,
    var day3ReminderSent: Boolean? = null,
    var deviceToken: String? = null,
    var deviceType: String? = null,
    var distanceSetting: Boolean? = null,
    var dob: Any? = null,
    var email: Any? = null,
    var ethnicity: Any? = null,
    var firstName: Any? = null,
    var hasABio: Boolean? = null,
    var isActive: Boolean? = null,
    var isBlocked: Boolean? = null,
    var isBoostActive: Boolean? = null,
    var isDeleted: Boolean? = null,
    var isMailOtpVerified: Boolean? = null,
    var isOnline: Boolean? = null,
    var isPhoneOtpVerified: Boolean? = null,
    var isUserOnHomePage: Boolean? = null,
    var isUserVerified: Int? = null,
    var language: String? = null,
    var lastActiveAt: String? = null,
    var lastName: Any? = null,
    var lastOnline: Any? = null,
    var location: Location? = null,
    var locationPermission: LocationPermission? = null,
    var locationSetting: Boolean? = null,
    var messageFilter: Int? = null,
    var notificationPermission: Boolean? = null,
    var notificationSetting: Int? = null,
    var notifyOnline: Boolean? = null,
    var phoneNumber: Any? = null,
    var privateAccount: Boolean? = null,
    var profileImages: List<Any?>? = null,
    var profileStatus: Int? = null,
    var rejectionReason: String? = null,
    var seeFilter: Int? = null,
    var socialType: String? = null,
    var socketId: List<Any?>? = null,
    var totalActivityScore: Int? = null,
    var uniqueId: String? = null,
    var updatedAt: Long? = null,

)

data class Location(
    var coordinates: List<Any?>? = null,
    var type: String? = null
)

data class LocationPermission(
    var allow: Boolean? = null,
    var deny: Boolean? = null
)
}