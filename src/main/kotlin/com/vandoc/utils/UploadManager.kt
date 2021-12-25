package com.vandoc.utils

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Bucket
import io.ktor.http.content.*
import java.util.*

class UploadManager(private val bucket: Bucket) {

    fun uploadToGoogleStorage(part: PartData.FileItem): String? {
        return try {
            val originalFileName = part.originalFileName as String
            val fileName = originalFileName.substringBeforeLast(".")
            val extension = originalFileName.substringAfterLast(".")
            val timestamp = System.currentTimeMillis()
            val newFileName = "$fileName-$timestamp.$extension"
            val contentType = part.contentType.toString()
            val file = part.streamProvider()

            val uuid = UUID.randomUUID().toString()
            val bucketName = System.getenv("BUCKET_NAME")
            val metadata = mapOf("firebaseStorageDownloadTokens" to uuid)
            val blobId = BlobId.of(bucketName, newFileName)
            val blobInfo = BlobInfo.newBuilder(blobId)
                .setMetadata(metadata)
                .setContentType(contentType)
                .build()

            bucket.storage.create(blobInfo, file.readBytes())

            "https://firebasestorage.googleapis.com/v0/b/${bucketName}/o/${newFileName}?alt=media&token=${uuid}"
        } catch (exception: Exception) {
            null
        }
    }

}