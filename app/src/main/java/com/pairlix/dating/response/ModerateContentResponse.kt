package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName
data class ModerateContentResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
){

data class Data(
    @SerializedName("abusiveTextDetected")
    var abusiveTextDetected: Boolean?,
    @SerializedName("moderationLabels")
    var moderationLabels: List<ModerationLabel?>?,
    @SerializedName("nudityDetected")
    var nudityDetected: Boolean?
)}

data class ModerationLabel(
    @SerializedName("documentImageUrl")
    var documentImageUrl: String?,
    @SerializedName("imageKey")
    var imageKey: String?,
    @SerializedName("isNudity")
    var isNudity: Boolean?,
    @SerializedName("labels")
    var labels: List<Label?>?,
    @SerializedName("type")
    var type: String?
)

data class Label(
    @SerializedName("Confidence")
    var confidence: Double?,
    @SerializedName("Name")
    var name: String?,
    @SerializedName("ParentName")
    var parentName: String?,
    @SerializedName("TaxonomyLevel")
    var taxonomyLevel: Int?
)