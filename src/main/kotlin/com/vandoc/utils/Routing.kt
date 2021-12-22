package com.vandoc.utils

import com.vandoc.model.response.Response
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

suspend fun <T> ApplicationCall.ok(message: String, data: T?) {
    response.status(HttpStatusCode.OK)
    respond(
        Response(message, data)
    )
}

suspend fun ApplicationCall.badRequest(message: String) {
    response.status(HttpStatusCode.BadRequest)
    respond(
        Response(message, null)
    )
}
