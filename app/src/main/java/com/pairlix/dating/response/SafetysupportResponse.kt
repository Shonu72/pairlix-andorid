package com.pairlix.dating.response

data class SafetyAndSupportResponse(
    var `data`: List<Data?>? = null,
    var message: String? = null,
    var success: Boolean? = null
)
{
data class Data(
    var description: String? = null,
    var image: String? = null,
    var title: String? = null
)}