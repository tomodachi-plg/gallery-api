package com.vandoc.utils

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)"
    return emailRegex.toRegex().matches(email)
}