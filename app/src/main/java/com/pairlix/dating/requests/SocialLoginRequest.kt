package com.pairlix.dating.requests

data class SocialLoginRequest(
    var deviceToken: String? = null,
    var deviceType: Int? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var socialType: Int? = null,
    var uniqueId: String? = null,
    val currentLongitude: String? = null,
    val currentLatitude: String? = null,
)