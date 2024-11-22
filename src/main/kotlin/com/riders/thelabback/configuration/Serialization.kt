package com.riders.thelabback.configuration

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

/**
 * Configuration for serialization of data classes
 *
 * This function configures the application to use JSON serialization for response bodies.
 * You can add other serialization formats if needed, such as XML or Protobuf.
 *
 * Learn more about serialization in Ktor: https://ktor.io/docs/serialization.html#json-serialization.
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}