package com.vandoc.model.response.user

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val user: UserResponse,
    val token: String
) {
    data class UserResponse(
        @SerializedName("fullname")
        val fullName: String,
        val username: String,
        val email: String
    )
}