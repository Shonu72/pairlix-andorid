package com.pairlix.dating.response

data class GetTicketResponse(
    var `data`: List<Data?>? = null,
    var message: String? = null,
    var success: Boolean? = null
) {
    data class Data(
        var _id: String? = null,
        var attachImage: List<String?>? = null,
        var createdAt: String? = null,
        var description: String? = null,
        var status: Int? = null,
        var resolvedReason: String? = null,
        var adminAction: String? = null,
        var resolvedAt: String? = null,
        var ticketId: Long? = null,
        var ticketType: String? = null,
        var titleName: String? = null,
        var updatedAt: String? = null,
        var userId: String? = null
    )
}