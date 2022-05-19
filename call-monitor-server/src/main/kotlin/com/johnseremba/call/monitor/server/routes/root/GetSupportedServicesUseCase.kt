package com.johnseremba.call.monitor.server.routes.root

import com.johnseremba.call.monitor.server.data.Response
import com.johnseremba.call.monitor.server.data.Services
import com.johnseremba.call.monitor.server.data.SupportedServicesResponse
import com.johnseremba.call.monitor.server.domain.GetIpAddressUseCase
import com.johnseremba.call.monitor.server.service.HttpServiceWorker.Companion.PORT
import java.util.*

internal class GetSupportedServicesUseCase(
    val getIpAddressUseCase: GetIpAddressUseCase,
    val getCallMonitorStartDateUseCase: GetCallMonitorStartDateUseCase
) {
    operator fun invoke(): Response<SupportedServicesResponse> {
        val ipAddress = getIpAddressUseCase()
        val baseUrl = "http://$ipAddress:$PORT"

        return Response(
            SupportedServicesResponse(
                start = getMonitoringDate(),
                services = listOf(
                    Services(name = "status", uri = "$baseUrl/status"),
                    Services(name = "log", uri = "$baseUrl/log")
                )
            )
        )
    }

    private fun getMonitoringDate(): Date? {
        return getCallMonitorStartDateUseCase()
    }
}

