package com.pairlix.dating.response

data class HelpResponse(
    var `data`: Data? = null,
    var message: String? = null,
    var success: Boolean? = null
)
{
data class Data(
    var countryCode: String? = null,
    var description: String? = null,
    var email: String? = null,
    var phoneNumber: String? = null
)}