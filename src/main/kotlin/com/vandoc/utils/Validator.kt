package com.vandoc.utils

import java.io.FileInputStream
import java.net.URLConnection

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)"
    return emailRegex.toRegex().matches(this)
}

fun String.isImageFile(): Boolean {
    return this.startsWith("image")
}

fun FileInputStream.isImageFile(): Boolean {
    return URLConnection.guessContentTypeFromStream(this).startsWith("image")
}

fun String.isVideoFile(): Boolean {
    return this.startsWith("video")
}

fun FileInputStream.isVideoFile(): Boolean {
    return URLConnection.guessContentTypeFromStream(this).startsWith("video")
}
