package com.pairlix.dating.res

data class CallStatusResponse(
    var `data`: Data? = null,
    var message: String? = null,
    var success: Boolean? = null
)

data class Data(
    var roomId: String? = null,
    var status: String? = null
)