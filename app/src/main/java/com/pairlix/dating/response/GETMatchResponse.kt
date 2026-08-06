package com.pairlix.dating.response

import com.google.gson.annotations.SerializedName

data class GetMatchResponse(
    @SerializedName("data")
    var `data`: List<Data?>? = listOf(),
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
) {
    data class Data(
        @SerializedName("action")
        var action: String? = "",
        @SerializedName("matchSource")
        var matchSource: String? = "",
        @SerializedName("age")
        var age: Int? = 0,
        @SerializedName("city")
        var city: String? = "",
        @SerializedName("cityAr")
        var cityAr: String? = "",
        @SerializedName("countryCode")
        var countryCode: String? = "",
        @SerializedName("countryName")
        var countryName: String? = "",
        @SerializedName("countryNameAr")
        var countryNameAr: String? = "",
        @SerializedName("distanceAway")
        var distanceAway: Double? = 0.0,
        @SerializedName("firstName")
        var firstName: String? = "",
        @SerializedName("gender")
        var gender: String? = "",
        @SerializedName("isOnline")
        var isOnline: Boolean? = false,
        @SerializedName("isLiked")
        var isLiked: Boolean? = false,
        @SerializedName("isRejected")
        var isRejected: Boolean? = false,
        @SerializedName("lastName")
        var lastName: String? = "",
        @SerializedName("matchCreatedAt")
        var matchCreatedAt: String? = "",
        @SerializedName("matchScore")
        var matchScore: Double? = 0.0,
        @SerializedName("finalRankScore")
        var finalRankScore: Double? = 0.0,
        @SerializedName("matchedFields")
        var matchedFields: List<String?>? = listOf(),
        @SerializedName("personalDetails")
        var personalDetails: PersonalDetails? = PersonalDetails(),
        @SerializedName("planType")
        var planType: Int? = 0,
        @SerializedName("activePlanType")
        var activePlanType: Int? = 0,
        @SerializedName("profileImages")
        var profileImages: List<String?>? = listOf(),
        @SerializedName("userId")
        var userId: String? = "",
        @SerializedName("isMatch")
        var isMatch: Boolean? = false,
        @SerializedName("canMessage")
        var canMessage: Boolean? = false,
        @SerializedName("isActive")
        var isActive: Boolean? = false,
     /*   @SerializedName("updatedAt")
        var updatedAt: Long? = 0,
        @SerializedName("createdAt")
        var createdAt: Long? = 0,*/
        @SerializedName("updatedAt")
        var updatedAt: String? = null,
        @SerializedName("createdAt")
        var createdAt: String? = null,
        @SerializedName("lastOnline")
        var lastOnline: Any? = Any(),
        @SerializedName("messageFilter")
        var messageFilter: Int? = 0,
        @SerializedName("seeFilter")
        var seeFilter: Int? = 0,
        @SerializedName("blurProfile")
        var blurProfile: Boolean? = false,
        @SerializedName("profileStatus")
        var profileStatus: Int? = 0,
        @SerializedName("ageSetting")
        var ageSetting: Boolean? = false,
        @SerializedName("distanceSetting")
        var distanceSetting: Boolean? = false,
        @SerializedName("locationSetting")
        var locationSetting: Boolean? = false,
        @SerializedName("isBoostActive")
        var isBoostActive: Boolean? = false,
        @SerializedName("boostEndTime")
        var boostEndTime: String?,
        ) {
        data class PersonalDetails(
            @SerializedName("aboardAfterMarriage")
            var aboardAfterMarriage: String? = "",
            @SerializedName("companyName")
            var companyName: String? = "",
            @SerializedName("currentProfession")
            var currentProfession: String? = "",
            @SerializedName("description")
            var description: String? = "",
            @SerializedName("educationLevel")
            var educationLevel: String? = "",
            @SerializedName("faith")
            var faith: List<Faith?>? = listOf(),
            @SerializedName("haveChildren")
            var haveChildren: String? = "",
            @SerializedName("height")
            var height: String? = "",
            @SerializedName("heightType")
            var heightType: String? = "",
            @SerializedName("howOftenDrink")
            var howOftenDrink: String? = "",
            @SerializedName("howOftenSmoke")
            var howOftenSmoke: String? = "",
            @SerializedName("images")
            var images: List<String?>? = listOf(),
            @SerializedName("interestedIn")
            var interestedIn: String? = "",
            @SerializedName("interests")
            var interests: List<Interest?>? = listOf(),
            @SerializedName("isDocumentVerified")
            var isDocumentVerified: Boolean? = false,
            @SerializedName("isFaceVerified")
            var isFaceVerified: Boolean? = false,
            @SerializedName("jobTitle")
            var jobTitle: String? = "",
            @SerializedName("maritalStatus")
            var maritalStatus: String? = "",
            @SerializedName("religionPractice")
            var religionPractice: String? = "",
            @SerializedName("schoolName")
            var schoolName: String? = "",
            @SerializedName("sect")
            var sect: String? = "",
            @SerializedName("spokenLanguages")
            var spokenLanguages: List<String?>? = listOf(),
            @SerializedName("workOut")
            var workOut: String? = "",

        ) {
            data class Faith(
                @SerializedName("faithNameAr")
                var faithNameAr: String? = "",
                @SerializedName("faithNameEn")
                var faithNameEn: String? = "",
                @SerializedName("iconImage")
                var iconImage: String? = "",
                @SerializedName("_id")
                var id: String? = ""
            )

            data class Interest(
                @SerializedName("categoryId")
                var categoryId: String? = "",
                @SerializedName("categoryNameAr")
                var categoryNameAr: String? = "",
                @SerializedName("categoryNameEn")
                var categoryNameEn: String? = "",
                @SerializedName("tags")
                var tags: List<Tag?>? = listOf()
            ) {

                data class Tag(
                    @SerializedName("iconImage")
                    var iconImage: String? = "",
                    @SerializedName("_id")
                    var id: String? = "",
                    @SerializedName("tagNameAr")
                    var tagNameAr: String? = "",
                    @SerializedName("tagNameEn")
                    var tagNameEn: String? = ""
                )
            }


        }

    }
}