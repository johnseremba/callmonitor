package com.johnseremba.call.monitor.server.routes.call_logs

import android.util.Log
import com.johnseremba.call.monitor.server.data.CallEntry
import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject

internal class CallLogRouteManager {
    private val getCallLogsUseCase: GetCallLogsUseCase by inject(GetCallLogsUseCase::class.java)

    fun getCallLogs(call: ApplicationCall) = runBlocking {
        val callLogs: List<CallEntry> = getCallLogsUseCase().data
        Log.v("CallLogs", callLogs.toString())
        call.respond(callLogs)
    }
}
