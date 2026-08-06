package com.pairlix.dating.response
import com.google.gson.annotations.SerializedName

data class GetAllCategoriesResponse(
    @SerializedName("data")
    val `data`: List<Data?>? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("success")
    var success: Boolean? = null
) {
    data class Data(
        @SerializedName("categoryName")
        val categoryName: CategoryName? = null,
        @SerializedName("id")
        val id: String? = null,
        @SerializedName("tags")
        var tags: List<Tag?>? = null
    ) {
        data class CategoryName(
            @SerializedName("ar")
            val ar: String? = null,
            @SerializedName("en")
            val en: String? = null
        )

        data class Tag(
            @SerializedName("iconImage")
            val iconImage: String? = null,
            @SerializedName("id")
            val id: String? = null,
            @SerializedName("tagName")
            val tagName: TagName? = null
        ) {
            data class TagName(
                @SerializedName("ar")
                val ar: String? = null,
                @SerializedName("en")
                val en: String? = null
            )
        }
    }
}