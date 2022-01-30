package com.riders.thelabback

import com.riders.thelabback.data.model.api.ApiResponse
import com.riders.thelabback.data.model.login.registerLoginRoute
import com.riders.thelabback.data.model.order.registerOrderRoutes
import com.riders.thelabback.data.model.user.registerUsersRoute
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json

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


    // Json Content Negotiation
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    registerUsersRoute()
    registerLoginRoute()
    registerOrderRoutes()

    routing {
        get("/") {
//            call.respondText("Hello, world!", contentType = ContentType.Text.Plain)
            call.respond(HttpStatusCode.OK, ApiResponse("Hello World!", HttpStatusCode.OK.value))
        }

        get("/connect") {
//            call.respondText("Hello, world!", contentType = ContentType.Text.Plain)
            call.respond(HttpStatusCode.OK, ApiResponse("TheLabBack Api is live", HttpStatusCode.OK.value))
        }

        get("/about") {
            call.respondText("About - Author MichaelStH", contentType = ContentType.Text.Plain)
        }
    }

}
