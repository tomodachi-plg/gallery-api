package com.vandoc.utils

import com.vandoc.model.response.Response
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

// 2XX
suspend fun <T> ApplicationCall.ok(message: String, data: T?) {
    response.status(HttpStatusCode.OK)
    respond(
        Response(message, data)
    )
}

// 4XX
suspend fun ApplicationCall.badRequest(message: String) {
    response.status(HttpStatusCode.BadRequest)
    respond(
        Response(message, null)
    )
}

suspend fun ApplicationCall.unauthorized(message: String) {
    response.status(HttpStatusCode.Unauthorized)
    respond(
        Response(message, null)
    )
}

// 5XX
suspend fun ApplicationCall.serverError(message: String) {
    response.status(HttpStatusCode.InternalServerError)
    respond(
        Response(message, null)
    )
}
