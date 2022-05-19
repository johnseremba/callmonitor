package com.johnseremba.call.monitor.server.routes.status

import android.util.Log
import com.johnseremba.call.monitor.server.data.CallMonitor
import com.johnseremba.call.monitor.server.data.CallState
import com.johnseremba.call.monitor.server.data.CallStatusResponse
import com.johnseremba.call.monitor.server.data.CallStatusResponse.CallStatus
import com.johnseremba.call.monitor.server.data.CallStatusResponse.NoData

internal class GetCallStatusUseCase(private val callMonitorFSM: CallMonitor) {
    operator fun invoke(): CallStatusResponse {
        return when (callMonitorFSM.getCurrentState()) {
            is CallState.Ringing, is CallState.CallAnswered -> {
                callMonitorFSM.queryStatus()
                val callEntry = callMonitorFSM.getCallLog()
                Log.v("GetCallStatusUseCase", callEntry.toString())

                CallStatus(
                    ongoing = true,
                    number = callEntry.number.orEmpty(),
                    name = callEntry.name.orEmpty()
                )
            }
            else -> NoData()
        }
    }
}
