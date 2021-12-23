package com.vandoc.model.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("fullname")
    val fullName: String?,
    val username: String?,
    val email: String?,
    val password: String?,
)