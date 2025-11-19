package com.riders.thelabback.di

import io.ktor.server.application.*
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

private val modules = provideApplicationModules()

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        includes(config) //can include external configuration extension
        modules(modules)
    }
}

fun Application.configureDI() {
    install(Koin) {
        slf4jLogger()
        modules(modules)
    }
}