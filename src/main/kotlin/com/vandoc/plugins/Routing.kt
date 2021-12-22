package com.vandoc.plugins

import com.vandoc.routes.registerAuthenticationRoutes
import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureRouting() {

    routing {
        registerAuthenticationRoutes()
    }

}
