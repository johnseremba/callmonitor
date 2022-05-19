package com.johnseremba.call.monitor.server.routes.root

import com.johnseremba.call.monitor.server.data.Response
import com.johnseremba.call.monitor.server.data.SupportedServicesResponse
import org.koin.java.KoinJavaComponent.inject

internal class RootRouteManager {
    private val servicesUseCase: GetSupportedServicesUseCase by inject(GetSupportedServicesUseCase::class.java)

    fun getServices(): Response<SupportedServicesResponse> = servicesUseCase()
}
