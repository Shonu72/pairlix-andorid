package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class PreviewProfileResponse(
    @SerializedName("data")
    var `data`: Data? ,
    @SerializedName("planType")
    var planType: Int? = null,
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
) {
    data class Data(
        @SerializedName("age")
        var age: Int? = 0,
        @SerializedName("socialType")
        var socialType: Int? = 0,
        @SerializedName("notificationSetting")
        var notificationSetting: Int? = 1,
        @SerializedName("city")
        var city: String? = "",
        @SerializedName("cityAr")
        var cityAr: String? = "",
        @SerializedName("countryCode")
        var countryCode: String? = "",
        @SerializedName("countryIso")
        var countryIso: String? = "",
        @SerializedName("countryName")
        var countryName: String? = "",
        @SerializedName("countryNameAr")
        var countryNameAr: String? = "",
        @SerializedName("matchScore")
        var matchScore: Double? =0.0,
        @SerializedName("dob")
        var dob: String? = "",
        @SerializedName("email")
        var email: String? = "",
        @SerializedName("firstName")
        var firstName: String? = "",
        @SerializedName("gender")
        var gender: String? = "",
        @SerializedName("_id")
        var id: String? = "",
        @SerializedName("isProfileCompleted")
        var isProfileCompleted: Boolean? = false,
        @SerializedName("isUserVerified")
        var isUserVerified: Int? = 0,
        @SerializedName("lastName")
        var lastName: String? = "",
        @SerializedName("personalDetails")
        var personalDetails: PersonalDetails? = PersonalDetails(),
        @SerializedName("phoneNumber")
        var phoneNumber: String? = "",
        @SerializedName("profileImages")
        var profileImages: List<String?>? = listOf(),
        @SerializedName("messageFilter")
        var messageFilter: Int? = 0,
        @SerializedName("seeFilter")
        var seeFilter: Int? = 0,
        @SerializedName("profileStatus")
        var profileStatus: Int? = 0,
        @SerializedName("ageSetting")
        var ageSetting: Boolean? = false,
        @SerializedName("blurProfile")
        var blurProfile: Boolean? = false,
        @SerializedName("distanceSetting")
        var distanceSetting: Boolean? = false,
        @SerializedName("locationSetting")
        var locationSetting: Boolean? = false,
        @SerializedName("isActive")
        var isActive: Boolean? = false,
        @SerializedName("isBoostActive")
        var isBoostActive: Boolean? = false,
        @SerializedName("boostEndTime")
        var boostEndTime: String?,
        @SerializedName("activePlanType")
        val activePlanType: Int? = null,

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
            var customProfession: String? = "",
            @SerializedName("customSect")
            var customSect: String? = null,
            @SerializedName("description")
            var description: String? = "",
            @SerializedName("educationLevel")
            var educationLevel: String? = "",
            @SerializedName("extraData")
            var extraData: String? = "",
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
            @SerializedName("_id")
            var id: String? = "",
            @SerializedName("imageHeight")
            var imageHeight: Any? = Any(),
            @SerializedName("images")
            var images: List<String>? = arrayListOf(),

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
            @SerializedName("sleepingHabit")
            var sleepingHabit: String? = "",
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
                @SerializedName("categoryNameAr")
                var categoryNameAr: String? = "",
                @SerializedName("categoryNameEn")
                var categoryNameEn: String? = "",
                @SerializedName("_id")
                var id: String? = "",
                @SerializedName("tags")
                var tags: List<Tag?>? = listOf()
            ) {
                data class Tag(
                    @SerializedName("createdAt")
                    var createdAt: Long? = 0,
                    @SerializedName("iconImage")
                    var iconImage: String? = "",
                    @SerializedName("_id")
                    var id: String? = "",
                    @SerializedName("interestCount")
                    var interestCount: Int? = 0,
                    @SerializedName("isBlocked")
                    var isBlocked: Boolean? = false,
                    @SerializedName("isDeleted")
                    var isDeleted: Boolean? = false,
                    @SerializedName("tagNameAr")
                    var tagNameAr: String? = "",
                    @SerializedName("tagNameEn")
                    var tagNameEn: String? = "",
                    @SerializedName("updatedAt")
                    var updatedAt: Long? = 0
                )
            }
        }
      /*  @SerializedName("planType")
        var planType: Int? = 1*/
    }
}
