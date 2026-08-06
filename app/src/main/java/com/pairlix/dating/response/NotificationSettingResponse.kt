package com.pairlix.dating.response

data class NotificationSettingResponse(
    var `data`: Data? = null,
    var message: String? = null,
    var success: Boolean? = null
)
{
data class Data(
    var _id: String? = null,
    var notificationSetting: Int? = null
)}