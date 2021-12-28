package com.riders.thelabback

import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit {
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    println("Hello World!")

    EngineMain.main(args)
}

@JvmOverloads
fun Application.module(testing: Boolean = true) {
    val port = environment.config.propertyOrNull("ktor.deployment.port")?.getString() ?: "8080"

    if (testing) {
        // Log used port
        println("Listening on port $port")
    }

    embeddedServer(Netty) {
    }
}
