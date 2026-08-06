package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class CompleteProfileResponse(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("success")
    var success: Boolean? = null
) {
    data class Data(
        @SerializedName("currentStep")
        val currentStep: Int? = null,
        @SerializedName("isProfileCompleted")
        val isProfileCompleted: Boolean? = null,
        @SerializedName("personalDetails")
        val personalDetails: PersonalDetails? = null,
        @SerializedName("profileCompletionPercentage")
        val profileCompletionPercentage: Int? = null,
        @SerializedName("userId")
        val userId: String? = null
    ) {
        data class PersonalDetails(
            @SerializedName("aboardAfterMarriage")
            val aboardAfterMarriage: String? = null,
            @SerializedName("companyName")
            val companyName: String? = null,
            @SerializedName("createdAt")
            val createdAt: String? = null,
            @SerializedName("currentProfession")
            val currentProfession: String? = null,
            @SerializedName("currentStep")
            val currentStep: Int? = null,
            @SerializedName("customSect")
            val customSect: Any? = null,
            @SerializedName("description")
            val description: String? = null,
            @SerializedName("educationLevel")
            val educationLevel: String? = null,
            @SerializedName("extraData")
            val extraData: String? = null,
            @SerializedName("faith")
            val faith: List<String?>? = null,
            @SerializedName("haveChildren")
            val haveChildren: String? = null,
            @SerializedName("height")
            val height: String? = null,
            @SerializedName("heightType")
            val heightType: Any? = null,
            @SerializedName("howOftenDrink")
            val howOftenDrink: String? = null,
            @SerializedName("howOftenSmoke")
            val howOftenSmoke: String? = null,
            @SerializedName("_id")
            val id: String? = null,
            @SerializedName("imageHeight")
            val imageHeight: Any? = null,
            @SerializedName("images")
            val images: List<String?>? = null,
            @SerializedName("interestedIn")
            val interestedIn: String? = null,
            @SerializedName("interests")
            val interests: List<Interest?>? = null,
            @SerializedName("isDocumentVerified")
            val isDocumentVerified: Boolean? = null,
            @SerializedName("isFaceVerified")
            val isFaceVerified: Boolean? = null,
            @SerializedName("jobTitle")
            val jobTitle: String? = null,
            @SerializedName("maritalStatus")
            val maritalStatus: String? = null,
            @SerializedName("profileCompletionPercentage")
            val profileCompletionPercentage: Int? = null,
            @SerializedName("religionPractice")
            val religionPractice: String? = null,
            @SerializedName("schoolName")
            val schoolName: String? = null,
            @SerializedName("sect")
            val sect: String? = null,
            @SerializedName("sleepingHabit")
            val sleepingHabit: String? = null,
            @SerializedName("spokenLanguages")
            val spokenLanguages: List<String?>? = null,
            @SerializedName("updatedAt")
            val updatedAt: String? = null,
            @SerializedName("uploadIdBack")
            val uploadIdBack: String? = null,
            @SerializedName("uploadIdFront")
            val uploadIdFront: String? = null,
            @SerializedName("workOut")
            val workOut: String? = null
        ) {
            data class Interest(
                @SerializedName("categoryId")
                val categoryId: String? = null,
                @SerializedName("_id")
                val id: String? = null,
                @SerializedName("tagIds")
                val tagIds: List<String?>? = null
            )
        }
    }
}