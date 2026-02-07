package com.serkomma.musicmap.app.ktor

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(true)
}

fun Application.module() {
    configureHTTP()
//    configureMonitoring()
//    configureSerialization()
//    configureDatabases()
    configureRouting()
}
