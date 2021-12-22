package com.vandoc.model.response

data class Response<T>(
    val message: String,
    val data: T?
)