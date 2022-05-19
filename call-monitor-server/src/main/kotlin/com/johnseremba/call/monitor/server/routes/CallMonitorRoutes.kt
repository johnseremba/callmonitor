package com.johnseremba.call.monitor.server.routes

import com.johnseremba.call.monitor.server.data.CallStatusResponse
import com.johnseremba.call.monitor.server.data.Response
import com.johnseremba.call.monitor.server.routes.call_logs.CallLogRouteManager
import com.johnseremba.call.monitor.server.routes.root.RootRouteManager
import com.johnseremba.call.monitor.server.routes.status.StatusRouteManager
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

internal fun Route.callMonitorRoutes() {
    get("/") {
        val services = RootRouteManager().getServices()
        call.respond(services.data)
    }

    get("/status") {
        val status: Response<CallStatusResponse> = StatusRouteManager().getCallStatus()
        call.respond(status.data)
    }

    get("/log") {
        CallLogRouteManager().getCallLogs(call)
    }
}
