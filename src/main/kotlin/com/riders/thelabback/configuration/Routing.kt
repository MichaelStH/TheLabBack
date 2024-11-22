package com.riders.thelabback.configuration


import com.riders.thelabback.data.model.api.ApiResponse
import com.riders.thelabback.data.model.login.registerLoginRoute
import com.riders.thelabback.data.model.order.registerOrderRoutes
import com.riders.thelabback.data.model.user.registerUsersRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Configures the routing for the [io.ktor.server.application.Application] object
 *
 * @return Unit
 */
fun Application.configureRouting() {

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

    // Add more routes here as needed
    registerLoginRoute()
    registerOrderRoutes()
    registerUsersRoute()
}
