package com.vandoc.routes

import com.vandoc.model.db.Gallery
import com.vandoc.model.request.gallery.CreateGalleryRequest
import com.vandoc.model.response.gallery.CreateGalleryResponse
import com.vandoc.utils.badRequest
import com.vandoc.utils.ok
import com.vandoc.utils.serverError
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

fun Routing.registerGalleryRoutes() {
    val database by inject<CoroutineDatabase>()

    authenticate {
        post("/gallery") {
            val body = call.receive<CreateGalleryRequest>()

            if (body.caption.isNullOrBlank()) {
                call.badRequest("caption can't be empty!")
                return@post
            }

            if (body.imagesUrl.isNullOrEmpty()) {
                call.badRequest("images_url required at least 1!")
                return@post
            }

            val galleryCollection = database.getCollection<Gallery>("posts")
            val gallery = Gallery(
                caption = body.caption,
                imagesUrl = body.imagesUrl
            )

            galleryCollection.insertOne(gallery)

            val isSuccess = galleryCollection.findOne(Gallery::_id eq gallery._id) != null

            if (!isSuccess) {
                call.serverError("Failed to create post, please try again later.")
                return@post
            }

            val galleryResponse = CreateGalleryResponse(
                galleryId = gallery.galleryId,
                caption = gallery.caption,
                imagesUrl = gallery.imagesUrl
            )

            call.ok(
                message = "Post created successfully",
                data = galleryResponse
            )

        }

    }

}