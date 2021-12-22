package com.vandoc.model.db

import org.bson.types.ObjectId

data class User(
    val _id: ObjectId = ObjectId(),
    val fullName: String,
    val username: String,
    val email: String,
    val password: String
)
