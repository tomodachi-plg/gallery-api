package com.vandoc.routes

import com.vandoc.model.db.User
import com.vandoc.model.request.LoginRequest
import com.vandoc.model.response.user.LoginResponse
import com.vandoc.utils.badRequest
import com.vandoc.utils.generateJwt
import com.vandoc.utils.ok
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

fun Routing.registerAuthenticationRoutes() {
    val database by inject<CoroutineDatabase>()

    post("/login") {
        val user = call.receive<LoginRequest>()

        if (user.email.isNullOrBlank()) {
            call.badRequest("email can't be empty!")
            return@post
        }

        if (user.password.isNullOrBlank()) {
            call.badRequest("password can't be empty!")
            return@post
        }

        val userCollection = database.getCollection<User>("users")
        val userData = userCollection.findOne(User::email eq user.email, User::password eq user.password)

        if (userData == null) {
            call.badRequest("user not found")
            return@post
        }

        val token = generateJwt(userData.username, userData.email)
        val loginResponse = LoginResponse(
            user = LoginResponse.UserResponse(
                fullName = userData.fullName,
                username = userData.username,
                email = userData.email
            ),
            token = token
        )

        call.ok(
            message = "Login success",
            data = loginResponse
        )
    }

}