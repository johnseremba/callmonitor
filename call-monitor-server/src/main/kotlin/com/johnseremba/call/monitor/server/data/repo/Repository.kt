package com.johnseremba.call.monitor.server.data.repo

import com.johnseremba.call.monitor.server.data.CallEntry

internal interface Repository {
    fun saveCallLog(entry: CallEntry): Boolean

    fun getCallLogs(): List<CallEntry>
}
