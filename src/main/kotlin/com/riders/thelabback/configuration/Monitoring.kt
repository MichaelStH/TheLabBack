package com.riders.thelabback.configuration

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

/**
 * This function configures monitoring for the application.
 * It enables request and response logging at the INFO level.
 * It filters out logging requests that do not start with "/".
 *
 * Note: This is a basic example and may not cover all possible monitoring needs.
 * You may want to add additional logging, metrics, and alerting configurations based on your specific requirements.
 */
fun Application.configureMonitoring() {

    // Enable request and response logging
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
}