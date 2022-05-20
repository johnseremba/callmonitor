package com.johnseremba.call.monitor.ui.domain

import com.johnseremba.call.monitor.server.api.CallMonitorApi
import com.johnseremba.call.monitor.server.data.CallEntry

class FetchLogsUseCase(private val api: CallMonitorApi) {
    suspend operator fun invoke(): List<CallEntry> {
        return api.fetchCallLogs()
    }
}
