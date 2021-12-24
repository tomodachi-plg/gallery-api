package com.vandoc.model.response.gallery

import com.google.gson.annotations.SerializedName

data class DeleteGalleryResponse(
    @SerializedName("gallery_id")
    val galleryId: String,
    val caption: String,
    @SerializedName("images_url")
    val imagesUrl: List<String>
)