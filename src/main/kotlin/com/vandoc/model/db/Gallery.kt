package com.vandoc.model.db

import org.bson.types.ObjectId
import java.util.*

data class Gallery(
    val _id: ObjectId = ObjectId(),
    val galleryId: String = UUID.randomUUID().toString(),
    val caption: String,
    val imagesUrl: List<String>
)
