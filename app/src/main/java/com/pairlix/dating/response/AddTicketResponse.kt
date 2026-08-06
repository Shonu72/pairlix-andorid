package com.pairlix.dating.response

data class AddTicketResponse(
    var `data`: Data? = null,
    var message: String? = null,
    var success: Boolean? = null
)
{
data class Data(
    var _id: String? = null,
    var attachImage: List<String?>? = null,
    var description: String? = null,
    var ticketId: Long? = null,
    var ticketType: String? = null,
    var titleName: String? = null
)

}