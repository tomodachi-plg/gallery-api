package com.vandoc.model.request.authentication

data class LoginRequest(
    val email: String?,
    val password: String?
)
