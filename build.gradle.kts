val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val koinVersion: String by project
val mongoVersion: String by project
val jbcryptVersion: String by project
val firebaseVersion: String by project


plugins {
    application
    kotlin("jvm") version "1.6.10"
}

group = "com.vandoc"
version = "0.0.1"
application {
    mainClass.set("com.vandoc.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // Security
    implementation("org.mindrot:jbcrypt:$jbcryptVersion")

    // Database
    implementation("com.google.firebase:firebase-admin:$firebaseVersion")
    implementation("org.litote.kmongo:kmongo-coroutine:$mongoVersion")

    // Injection
    implementation("io.insert-koin:koin-ktor:$koinVersion")

    // Server
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-metrics:$ktorVersion")
    implementation("io.ktor:ktor-gson:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")

    // Logger
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Testing
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}

tasks.create("stage") {
    dependsOn("generateFirebaseServiceFile")
    dependsOn("installDist")
}

tasks.create("generateFirebaseServiceFile") {
    doLast {
        println(System.getenv())
        File(
            projectDir,
            System.getenv("SERVICE_PATH") + System.getenv("SERVICE_FILE_NAME")
        ).writeText(System.getenv("SERVICE_FILE"))
    }
}
