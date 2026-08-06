package com.pairlix.dating.response

data class FaqResponse(
    var `data`: List<Data?>? = null,
    var message: String? = null,
    var success: Boolean? = null
)
{
data class Data(
    var answer: String? = null,
    var question: String? = null,
)}