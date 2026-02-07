package com.serkomma.musicmap.app.ktor.v1

import com.serkomma.musicmap.app.ktor.AppSettings
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.v1Card(appSettings: AppSettings) {
    route("card"){
        post("create") {
            call.createAd(appSettings)
        }
        post("read") {
            call.readAd(appSettings)
        }
        post("update") {
            call.updateAd(appSettings)
        }
        post("delete") {
            call.deleteAd(appSettings)
        }
        post("search") {
            call.searchAd(appSettings)
        }
        post("offers") {
            call.offersAd(appSettings)
        }
    }
}