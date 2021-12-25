package com.vandoc.routes

import com.vandoc.utils.*
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.registerUploadRoutes() {
    val uploadManager by inject<UploadManager>()

    post("/upload") {
        val multipartData = call.receiveMultipart()

        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {}
                is PartData.FileItem -> {
                    if (part.name.isNullOrBlank()) {
                        call.badRequest("file can't be empty!")
                        return@forEachPart
                    }

                    val contentType = part.contentType.toString()

                    if (!contentType.isImageFile() && !contentType.isVideoFile()) {
                        call.badRequest("only image/video files supported!")
                        return@forEachPart
                    }

                    val uploadedUrl = uploadManager.uploadToGoogleStorage(part)

                    if (uploadedUrl.isNullOrBlank()) {
                        call.serverError("Failed to upload file, please try again later!")
                        return@forEachPart
                    }

                    call.ok(
                        message = "Success upload file",
                        data = uploadedUrl
                    )
                }
                is PartData.BinaryItem -> {}
            }
        }
    }

}