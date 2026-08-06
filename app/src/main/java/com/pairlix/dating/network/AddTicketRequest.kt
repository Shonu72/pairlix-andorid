package com.pairlix.dating.network

data class AddTicketRequest(
    var attachImage: List<String?>? = null,
    var description: String? = null,
    var ticketType: String? = null,
    var titleName: String? = null
)