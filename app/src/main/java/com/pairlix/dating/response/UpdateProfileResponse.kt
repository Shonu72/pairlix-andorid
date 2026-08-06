package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class UpdateProfileResponse(
    @SerializedName("data")
    var `data`: Data? = Data(),
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
) {
    data class Data(
        @SerializedName("personalDetails")
        var personalDetails: PersonalDetails? = PersonalDetails(),
        @SerializedName("user")
        var user: User? = User()
    ) {
        data class PersonalDetails(
            @SerializedName("aboardAfterMarriage")
            var aboardAfterMarriage: String? = "",
            @SerializedName("companyName")
            var companyName: String? = "",
            @SerializedName("createdAt")
            var createdAt: String? = "",
            @SerializedName("currentProfession")
            var currentProfession: String? = "",
            @SerializedName("currentStep")
            var currentStep: Int? = 0,
            @SerializedName("customProfession")
            var customProfession: Any? = Any(),
            @SerializedName("customSect")
            var customSect: Any? = Any(),
            @SerializedName("description")
            var description: String? = "",
            @SerializedName("educationLevel")
            var educationLevel: String? = "",
            @SerializedName("extraData")
            var extraData: String? = "",
            @SerializedName("faith")
            var faith: List<String?>? = listOf(),
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
            @SerializedName("_id")
            var id: String? = "",
            @SerializedName("imageHeight")
            var imageHeight: Any? = Any(),
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
            @SerializedName("profileCompletionPercentage")
            var profileCompletionPercentage: Int? = 0,
            @SerializedName("religionPractice")
            var religionPractice: String? = "",
            @SerializedName("schoolName")
            var schoolName: String? = "",
            @SerializedName("sect")
            var sect: String? = "",
            @SerializedName("spokenLanguages")
            var spokenLanguages: List<String?>? = listOf(),
            @SerializedName("updatedAt")
            var updatedAt: String? = "",
            @SerializedName("uploadIdBack")
            var uploadIdBack: String? = "",
            @SerializedName("uploadIdFront")
            var uploadIdFront: String? = "",
            @SerializedName("workOut")
            var workOut: String? = ""
        ) {
            data class Interest(
                @SerializedName("categoryId")
                var categoryId: String? = "",
                @SerializedName("_id")
                var id: String? = "",
                @SerializedName("tagIds")
                var tagIds: List<String?>? = listOf()
            )
        }

        data class User(
            @SerializedName("accessToken")
            var accessToken: Any? = Any(),
            @SerializedName("activePlan")
            var activePlan: String? = "",
            @SerializedName("age")
            var age: Int? = 0,
            @SerializedName("ageSettings")
            var ageSettings: Boolean? = false,
            @SerializedName("blurMyProfile")
            var blurMyProfile: Boolean? = false,
            @SerializedName("city")
            var city: String? = "",
            @SerializedName("continousMatches")
            var continousMatches: Any? = Any(),
            @SerializedName("countryCode")
            var countryCode: String? = "",
            @SerializedName("countryIso")
            var countryIso: String? = "",
            @SerializedName("countryName")
            var countryName: String? = "",
            @SerializedName("createdAt")
            var createdAt: Long? = 0,
            @SerializedName("deviceToken")
            var deviceToken: String? = "",
            @SerializedName("deviceType")
            var deviceType: String? = "",
            @SerializedName("dislikes")
            var dislikes: Any? = Any(),
            @SerializedName("distanceSettings")
            var distanceSettings: Boolean? = false,
            @SerializedName("dob")
            var dob: String? = "",
            @SerializedName("email")
            var email: String? = "",
            @SerializedName("ethnicity")
            var ethnicity: Any? = Any(),
            @SerializedName("firstName")
            var firstName: String? = "",
            @SerializedName("gender")
            var gender: String? = "",
            @SerializedName("hasABio")
            var hasABio: Boolean? = false,
            @SerializedName("_id")
            var id: String? = "",
            @SerializedName("isActive")
            var isActive: Boolean? = false,
            @SerializedName("isBlocked")
            var isBlocked: Boolean? = false,
            @SerializedName("isDeleted")
            var isDeleted: Boolean? = false,
            @SerializedName("isMailOtpVerified")
            var isMailOtpVerified: Boolean? = false,
            @SerializedName("isMatched")
            var isMatched: Boolean? = false,
            @SerializedName("isOnline")
            var isOnline: Boolean? = false,
            @SerializedName("isPhoneOtpVerified")
            var isPhoneOtpVerified: Boolean? = false,
            @SerializedName("isProfileCompleted")
            var isProfileCompleted: Boolean? = false,
            @SerializedName("isUserVerified")
            var isUserVerified: Int? = 0,
            @SerializedName("language")
            var language: String? = "",
            @SerializedName("lastActive")
            var lastActive: Boolean? = false,
            @SerializedName("lastLogin")
            var lastLogin: Any? = Any(),
            @SerializedName("lastName")
            var lastName: String? = "",
            @SerializedName("likes")
            var likes: Any? = Any(),
            @SerializedName("location")
            var location: Location? = Location(),
            @SerializedName("locationPermission")
            var locationPermission: LocationPermission? = LocationPermission(),
            @SerializedName("locationSettings")
            var locationSettings: Boolean? = false,
           /* @SerializedName("messageFilter")
            var messageFilter: MessageFilter? = MessageFilter(),*/
            @SerializedName("notificationPermission")
            var notificationPermission: Boolean? = false,
            @SerializedName("notifyOnline")
            var notifyOnline: Boolean? = false,
            @SerializedName("personalDetails")
            var personalDetails: String? = "",
            @SerializedName("phoneNumber")
            var phoneNumber: String? = "",
            @SerializedName("privateAccount")
            var privateAccount: Boolean? = false,
            @SerializedName("profileImages")
            var profileImages: List<String?>? = listOf(),
            @SerializedName("profileStatus")
            var profileStatus: Any? = Any(),
            @SerializedName("rejectionReason")
            var rejectionReason: String? = "",
    /*        @SerializedName("seeFilter")
            var seeFilter: SeeFilter? = SeeFilter(),*/
            @SerializedName("superlikes")
            var superlikes: Any? = Any(),
            @SerializedName("updatedAt")
            var updatedAt: Long? = 0
        ) {
            data class Location(
                @SerializedName("coordinates")
                var coordinates: List<Any?>? = listOf(),
                @SerializedName("type")
                var type: String? = ""
            )

            data class LocationPermission(
                @SerializedName("allow")
                var allow: Boolean? = false,
                @SerializedName("deny")
                var deny: Boolean? = false
            )

          /*  data class MessageFilter(
                @SerializedName("enum")
                var `enum`: List<Any?>? = listOf()
            )

            data class SeeFilter(
                @SerializedName("enum")
                var `enum`: List<Any?>? = listOf()
            )*/
        }
    }
}


