package com.johnseremba.call.monitor.server.data.repo

import com.johnseremba.call.monitor.server.data.CallEntry

internal interface Repository {
    suspend fun saveCallLog(entry: CallEntry): Boolean

    suspend fun getCallLogs(): List<CallEntry>
}
