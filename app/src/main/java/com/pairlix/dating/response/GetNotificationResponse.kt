package com.pairlix.dating.response

data class GetNotificationResponse(
    var `data`: List<Data?>? = null,
    var message: String? = null,
    var success: Boolean? = null
)
{
data class Data(
    var _id: String? = null,
    var descriptionAr: String? = null,
    var descriptionEn: String? = null,
    var id: String? = null,
    var notification_type: Int? = null,
    var readUser: List<Any?>? = null,
    var sendFrom: Int? = null,
    var sendTo: Int? = null,
    var timestamp: String? = null,
    var titleAr: String? = null,
    var titleEn: String? = null,
    var type: String? = null,
    var user: List<String?>? = null
)}