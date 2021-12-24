package com.vandoc.model.request.authentication

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("fullname")
    val fullName: String?,
    val username: String?,
    val email: String?,
    val password: String?,
)