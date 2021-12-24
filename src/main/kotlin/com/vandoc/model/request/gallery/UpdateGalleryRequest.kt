package com.vandoc.model.request.gallery

import com.google.gson.annotations.SerializedName

data class UpdateGalleryRequest(
    val caption: String?,
    @SerializedName("images_url")
    val imagesUrl: List<String>?
)