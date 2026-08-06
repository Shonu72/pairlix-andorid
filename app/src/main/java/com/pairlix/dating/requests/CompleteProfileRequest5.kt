package com.pairlix.dating.requests
import com.google.gson.annotations.SerializedName
data class CompleteProfileRequest5(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("step")
    val step: Int? = null
) {
    data class Data(
        @SerializedName("howOftenDrink")
        val howOftenDrink: String? = null,
        @SerializedName("howOftenSmoke")
        val howOftenSmoke: String? = null,
        @SerializedName("sleepingHabit")
        val sleepingHabit: String? = null,
        @SerializedName("workOut")
        val workOut: String? = null
    )
}


