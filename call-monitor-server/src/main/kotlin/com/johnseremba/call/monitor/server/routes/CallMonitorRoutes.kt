package com.johnseremba.call.monitor.server.routes

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

internal fun Route.callMonitorRoutes() {
    get("/") {
        call.respond(Response(data = listOf("/")))
    }

    get("/status") {
        call.respond(Response(data = "status"))
    }

    get("/log") {
        call.respond(Response(data = "logs"))
    }
}

data class Response<T>(
    val status: Int = 0,
    val data: T? = null,
)
