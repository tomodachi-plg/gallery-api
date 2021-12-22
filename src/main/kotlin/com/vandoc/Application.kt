package com.vandoc

import com.vandoc.plugins.configureAuthentication
import com.vandoc.plugins.configureMonitoring
import com.vandoc.plugins.configureRouting
import com.vandoc.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val port = System.getenv("PORT")?.toInt()
    embeddedServer(Netty, port = port ?: 8081) {
        configureInjection()
        configureRouting()
        configureAuthentication()
        configureMonitoring()
        configureSerialization()
    }.start(wait = true)
}
