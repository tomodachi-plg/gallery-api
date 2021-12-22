package com.vandoc

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.vandoc.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory

fun main() {
    val port = System.getenv("PORT")?.toInt()

    (LoggerFactory.getILoggerFactory() as LoggerContext).getLogger("org.mongodb.driver").level = Level.ERROR

    embeddedServer(Netty, port = port ?: 8081) {
        configureInjection()
        configureRouting()
        configureAuthentication()
        configureMonitoring()
        configureSerialization()
    }.start(wait = true)
}
