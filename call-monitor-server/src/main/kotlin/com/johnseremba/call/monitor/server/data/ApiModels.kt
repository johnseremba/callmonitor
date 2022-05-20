package com.johnseremba.call.monitor.server.data

import java.util.*

internal data class Response<T>(val data: T)

internal sealed class ErrorResponse {
    data class GenericError(val message: String = "Unexpected error occurred!") : ErrorResponse()
}

internal data class SupportedServicesResponse(
    val start: Date?,
    val services: List<Services>
)

internal data class Services(
    val name: String,
    val uri: String
)

internal sealed class CallStatusResponse {
    data class CallStatus(
        val ongoing: Boolean,
        val number: String,
        val name: String
    ) : CallStatusResponse()

    data class NoData(val message: String = "No data"): CallStatusResponse()
}
