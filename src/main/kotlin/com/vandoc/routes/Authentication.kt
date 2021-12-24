package com.vandoc.routes

import com.vandoc.model.db.User
import com.vandoc.model.request.authentication.LoginRequest
import com.vandoc.model.request.authentication.RegisterRequest
import com.vandoc.model.response.authentication.LoginResponse
import com.vandoc.model.response.authentication.RegisterResponse
import com.vandoc.utils.*
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.mindrot.jbcrypt.BCrypt

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
        val userData = userCollection.findOne(User::email eq user.email)

        if (userData == null || !BCrypt.checkpw(user.password, userData.password)) {
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

    post("/register") {
        val user = call.receive<RegisterRequest>()

        if (user.fullName.isNullOrBlank()) {
            call.badRequest("fullname can't be empty!")
            return@post
        }

        if (user.username.isNullOrBlank()) {
            call.badRequest("username can't be empty!")
            return@post
        }

        if (user.username.length < 6) {
            call.badRequest("username can't less than 6 characters!")
            return@post
        }

        val userCollection = database.getCollection<User>("users")
        val isUsernameExist = userCollection.findOne(User::username eq user.username) != null

        if (isUsernameExist) {
            call.badRequest("username already exists!")
            return@post
        }

        if (user.email.isNullOrBlank()) {
            call.badRequest("email can't be empty!")
            return@post
        }

        if (!isValidEmail(user.email)) {
            call.badRequest("email is malformed!")
            return@post
        }

        val isEmailExist = userCollection.findOne(User::email eq user.email) != null

        if (isEmailExist) {
            call.badRequest("email already exists!")
            return@post
        }

        if (user.password.isNullOrBlank()) {
            call.badRequest("password can't be empty!")
            return@post
        }

        if (user.password.length < 8) {
            call.badRequest("password can't less than 8 characters!")
            return@post
        }

        val hashedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())
        val newUser = User(
            fullName = user.fullName,
            username = user.username,
            email = user.email,
            password = hashedPassword
        )

        userCollection.insertOne(newUser)

        val isSuccess = userCollection.findOne(User::_id eq newUser._id) != null

        if (!isSuccess) {
            call.serverError("Failed to register user, please try again later.")
            return@post
        }

        val token = generateJwt(newUser.username, newUser.email)
        val registerResponse = RegisterResponse(
            user = RegisterResponse.UserResponse(
                fullName = newUser.fullName,
                username = newUser.username,
                email = newUser.email
            ),
            token = token
        )

        call.ok(
            message = "Register success",
            data = registerResponse
        )

    }

}