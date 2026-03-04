package com.serkomma.musicmap.app.ktor.v1

import com.serkomma.musicmap.app.ktor.AppSettings
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.v1Card(appSettings: AppSettings) {
    route("card"){
        post("create") {
            call.createCard(appSettings)
        }
        post("read") {
            call.readCard(appSettings)
        }
        post("update") {
            call.updateCard(appSettings)
        }
        post("delete") {
            call.deleteCard(appSettings)
        }
        post("search") {
            call.searchCard(appSettings)
        }
        post("offers") {
            call.offersCard(appSettings)
        }
    }
}