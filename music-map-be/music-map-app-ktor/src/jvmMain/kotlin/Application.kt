package com.serkomma.musicmap.app.ktor

import configureDatabases
import configureMonitoring
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureRouting()
    configureDatabases()
    routing {
        get("/memory") {
            val runtime = Runtime.getRuntime()
            val usedMemory = runtime.totalMemory() - runtime.freeMemory()
            call.respondText(usedMemory.toString())
        }
    }
}
