package com.serkomma.musicmap.app.ktor

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.serkomma.musicmap.api.apiMapper
import com.serkomma.musicmap.app.ktor.plugins.initAppSettings
import com.serkomma.musicmap.app.ktor.v1.v1Card

fun Application.configureRouting() {
    routing {
        install(ContentNegotiation) {
            json(apiMapper)
        }
        get("/") {
            call.respondText("Hello, world!")
        }
        route("v1") {
            v1Card(initAppSettings())
        }
    }
}
