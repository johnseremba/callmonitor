package com.johnseremba.call.monitor.server.api

import com.johnseremba.call.monitor.server.data.CallEntry
import com.johnseremba.call.monitor.server.data.repo.Repository
import org.koin.java.KoinJavaComponent.inject

class CallMonitorApi {
    private val repository: Repository by inject(Repository::class.java)

    suspend fun fetchCallLogs(): List<CallEntry> = repository.getCallLogs()
}
