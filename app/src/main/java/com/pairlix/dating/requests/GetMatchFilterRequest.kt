package com.pairlix.dating.requests
data class GetMatchFilterRequest(
    val countryName: String? = null,
    val city: String? = null,
    val minAge: Int? = null,
    val maxAge: Int? = null,
    val spokenLanguages: String? = null,
    val sect: Int? = null,
    val currentProfession: Int? = null,
    val maritalStatus: Int? = null,
    val howOftenDrink: Int? = null,
    val howOftenSmoke: Int? = null,
    val maxDistance: Int? = null,
    val planType: Int? = null,
    val haveChildren: Int? = null,
    val interestTags: String? = null,
    val interestedIn: Int? = null
)
