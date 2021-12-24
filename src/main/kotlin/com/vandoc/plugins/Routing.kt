package com.vandoc.plugins

import com.vandoc.routes.registerAuthenticationRoutes
import com.vandoc.routes.registerGalleryRoutes
import com.vandoc.utils.unauthorized
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.routing.*

fun Application.configureRouting() {

    install(StatusPages) {
        status(HttpStatusCode.Unauthorized) {
            call.unauthorized("Bad token or token already expired!")
        }
    }

    routing {
        registerAuthenticationRoutes()
        registerGalleryRoutes()
    }

}
