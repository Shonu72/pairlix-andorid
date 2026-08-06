package com.pairlix.dating.response

data class CheckAbusiveWordResponse(
    var `data`: Data? = null,
    var message: String? = null,
    var status: Int? = null
)
{
data class Data(
    var matchedWord: String? = null,
    var reason: String? = null,
    var safe: Boolean? = null,
    var score: Any? = null,
    var sentiment: Any? = null,
    var source: String? = null
)}