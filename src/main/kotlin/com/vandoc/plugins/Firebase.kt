package com.vandoc.plugins

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.application.*

fun Application.configureFirebase() {
    val serviceFileName = System.getenv("SERVICE_FILE_NAME")
    val bucketName = System.getenv("BUCKET_NAME")

    val serviceAccount = this::class.java.classLoader.getResourceAsStream(serviceFileName)
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setStorageBucket(bucketName)
        .build()


    FirebaseApp.initializeApp(options)
}