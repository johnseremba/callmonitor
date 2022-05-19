package com.johnseremba.call.monitor.server.routes.status

import com.johnseremba.call.monitor.server.data.CallStatusResponse
import com.johnseremba.call.monitor.server.data.Response
import org.koin.java.KoinJavaComponent.inject

internal class StatusRouteManager {
    private val getCallStatusUseCase: GetCallStatusUseCase by inject(GetCallStatusUseCase::class.java)

    fun getCallStatus(): Response<CallStatusResponse> {
        val callStatus: CallStatusResponse = getCallStatusUseCase()
        return Response(data = callStatus)
    }
}
