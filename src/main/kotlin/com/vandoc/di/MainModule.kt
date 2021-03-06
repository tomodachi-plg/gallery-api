package com.vandoc.di

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.google.firebase.cloud.StorageClient
import com.vandoc.utils.UploadManager
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import org.slf4j.LoggerFactory

val mainModule = module {

    single {
        val host = System.getenv("MONGO_HOST")
        val username = System.getenv("MONGO_USERNAME")
        val password = System.getenv("MONGO_PASSWORD")
        val cluster = System.getenv("MONGO_CLUSTER")
        val dbname = System.getenv("MONGO_DB_NAME")
        val query = "?retryWrites=true&w=majority"
        val connectionString = "$host://$username:$password@$cluster/$dbname$query"

        (LoggerFactory.getILoggerFactory() as LoggerContext).getLogger("org.mongodb.driver").level = Level.ERROR

        val client = KMongo.createClient(connectionString).coroutine
        client.getDatabase(dbname)
    }

    single { StorageClient.getInstance().bucket() }

    single { UploadManager(get()) }

}