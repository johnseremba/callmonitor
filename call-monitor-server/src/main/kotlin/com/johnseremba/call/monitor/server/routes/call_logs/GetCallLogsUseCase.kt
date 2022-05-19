package com.johnseremba.call.monitor.server.routes.call_logs

import com.johnseremba.call.monitor.server.data.CallEntry
import com.johnseremba.call.monitor.server.data.Response
import com.johnseremba.call.monitor.server.data.repo.Repository

internal class GetCallLogsUseCase(val repository: Repository) {
    suspend operator fun invoke(): Response<List<CallEntry>> {
        return Response(repository.getCallLogs())
    }
}
