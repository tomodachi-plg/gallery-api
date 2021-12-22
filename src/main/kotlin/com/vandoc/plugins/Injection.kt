package com.vandoc.plugins

import com.vandoc.di.mainModule
import io.ktor.application.*
import org.koin.ktor.ext.Koin

fun Application.configureInjection() {
    install(Koin) {
        modules(mainModule)
    }
}
