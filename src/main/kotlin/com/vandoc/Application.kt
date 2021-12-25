package com.vandoc

import com.vandoc.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val port = System.getenv("PORT")?.toInt()
    embeddedServer(Netty, port = port ?: 8081) {
        configureInjection()
        configureAuthentication()
        configureSerialization()
        configureFirebase()
        configureRouting()
        configureMonitoring()
    }.start(wait = true)
}
