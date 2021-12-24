package com.vandoc.routes

import com.vandoc.model.db.Gallery
import com.vandoc.model.request.gallery.CreateGalleryRequest
import com.vandoc.model.request.gallery.UpdateGalleryRequest
import com.vandoc.model.response.gallery.CreateGalleryResponse
import com.vandoc.model.response.gallery.DeleteGalleryResponse
import com.vandoc.model.response.gallery.GetGalleryResponse
import com.vandoc.model.response.gallery.UpdateGalleryResponse
import com.vandoc.utils.badRequest
import com.vandoc.utils.ok
import com.vandoc.utils.serverError
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import org.litote.kmongo.SetTo
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

fun Routing.registerGalleryRoutes() {
    val database by inject<CoroutineDatabase>()

    authenticate {

        route("/gallery") {

            get {
                val page = (call.request.queryParameters["page"] ?: "1").toInt()
                val limit = (call.request.queryParameters["limit"] ?: "25").toInt()

                if (page == 0) {
                    call.badRequest("Page can't less than 1!")
                    return@get
                }

                if (limit < 5) {
                    call.badRequest("Limit can't less than 5!")
                    return@get
                }

                val galleryCollection = database.getCollection<Gallery>("posts")
                val listGallery = galleryCollection.find().skip((page - 1) * limit).limit(limit).toList()

                val galleryResponse = listGallery.map {
                    GetGalleryResponse(
                        galleryId = it.galleryId,
                        caption = it.caption,
                        imagesUrl = it.imagesUrl
                    )
                }

                call.ok(
                    message = "Success get gallery",
                    data = galleryResponse
                )

            }

            post {
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
                    message = "Gallery created successfully",
                    data = galleryResponse
                )

            }

            put("/{gallery_id}") {
                val galleryId = call.parameters["gallery_id"]
                val body = call.receive<UpdateGalleryRequest>()

                if (galleryId.isNullOrBlank()) {
                    call.badRequest("gallery_id can't be empty!")
                    return@put
                }

                if (body.imagesUrl.isNullOrEmpty()) {
                    call.badRequest("images_url required at least 1!")
                    return@put
                }

                val galleryCollection = database.getCollection<Gallery>("posts")
                val updateFields = mutableListOf<SetTo<Any>>(
                    SetTo(Gallery::imagesUrl, body.imagesUrl)
                )

                if (!body.caption.isNullOrBlank()) {
                    updateFields.add(SetTo(Gallery::caption, body.caption))
                }

                val updateResult = galleryCollection.updateMany(
                    Gallery::galleryId eq galleryId,
                    *updateFields.toTypedArray()
                )

                if (updateResult.matchedCount < 1) {
                    call.badRequest("gallery not found!")
                    return@put
                }

                if (updateResult.modifiedCount < 1) {
                    call.badRequest("field can't be same as previous!")
                    return@put
                }

                val updatedGallery = galleryCollection.findOne(Gallery::galleryId eq galleryId)
                val galleryResponse = UpdateGalleryResponse(
                    galleryId = updatedGallery?.galleryId.orEmpty(),
                    caption = updatedGallery?.caption.orEmpty(),
                    imagesUrl = updatedGallery?.imagesUrl.orEmpty()
                )

                call.ok(
                    message = "Success update gallery",
                    data = galleryResponse
                )

            }

            delete("/{gallery_id}") {
                val galleryId = call.parameters["gallery_id"]

                if (galleryId.isNullOrBlank()) {
                    call.badRequest("gallery_id can't be empty!")
                    return@delete
                }

                val galleryCollection = database.getCollection<Gallery>("posts")
                val gallery = galleryCollection.findOne(Gallery::galleryId eq galleryId)

                if (gallery == null) {
                    call.badRequest("gallery_id not found!")
                    return@delete
                }

                val isSuccess = galleryCollection.deleteOne(Gallery::galleryId eq galleryId).deletedCount > 0

                if (!isSuccess) {
                    call.serverError("Failed to delete gallery, please try again later.")
                    return@delete
                }

                val galleryResponse = DeleteGalleryResponse(
                    galleryId = gallery.galleryId,
                    caption = gallery.caption,
                    imagesUrl = gallery.imagesUrl
                )

                call.ok(
                    message = "Gallery deleted successfully",
                    data = galleryResponse
                )

            }

        }

    }

}