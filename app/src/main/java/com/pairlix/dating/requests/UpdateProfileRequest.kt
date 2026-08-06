package com.pairlix.dating.requests

import com.google.gson.annotations.SerializedName


data class UpdateProfileRequest(
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
    @SerializedName("cityAr ")
    val cityAr : String? = null,
    @SerializedName("countryName")
    val countryName: String? = null,
    @SerializedName("countryNameAr ")
    val countryNameAr : String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    val longitude: String? = null,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
    @SerializedName("profileImages")
    val profileImages: String? = null,
    @SerializedName("personalDetails")
    var personalDetails: PersonalDetails? = null

) {
    data class PersonalDetails(
        @SerializedName("companyName")
        var companyName: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("haveChildren")
        var haveChildren: String? = null,
        @SerializedName("height")
        var height: String? = null,
        @SerializedName("howOftenDrink")
        var howOftenDrink: String? = null,

        @SerializedName("howOftenSmoke")
        var howOftenSmoke: String? = null,
        @SerializedName("jobTitle")
        var jobTitle: String? = null,
        @SerializedName("religionPractice")
        var religionPractice: String? = null,
        @SerializedName("sect")
        var sect: String? = null,
        @SerializedName("spokenLanguages")
        var spokenLanguages: List<String?>? = null,
        @SerializedName("maritalStatus")
        val maritalStatus: String? = null,
        @SerializedName("relocation")
        val relocation: String? = null,
        @SerializedName("images")
        val images: List<String?>? = null,
        @SerializedName("customSect")
        val customSect: String? = null,
        @SerializedName("interestedIn")
        val interestedIn: String? = null,
        @SerializedName("currentProfession")
        val currentProfession: String? = null,
        @SerializedName("customProfession")
        val customProfession: String? = null,
        @SerializedName("educationLevel")
        val educationLevel: String? = null,
        @SerializedName("schoolName")
        val schoolName: String? = null,
        @SerializedName("sleepingHabit")
        val sleepingHabit: String? = null,
        @SerializedName("workOut")
        val workOut: String? = null,
        @SerializedName("aboardAfterMarriage")
        val aboardAfterMarriage: String? = null,
        @SerializedName("faith")
        val faith: List<String?>? = null,
        @SerializedName("heightType")
        val heightType: String? = null,
        @SerializedName("isFaceVerified")
        val isFaceVerified: Boolean? = null,
        @SerializedName("extraData")
        val extraData: String? = null,
        @SerializedName("uploadIdBack")
        val uploadIdBack: String? = null,
        @SerializedName("uploadIdFront")
        val uploadIdFront: String? = null,
        @SerializedName("interests")
        val interests: List<Interest>? = null

    )
    data class Interest(
        @SerializedName("categoryId")
        val categoryId: String? = null,
        @SerializedName("tagIds")
        val tagIds: List<String?>? = null
    )

}




