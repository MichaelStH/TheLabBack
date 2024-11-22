package com.riders.thelabback.data.model.login

import com.riders.thelabback.data.model.api.ApiResponse
import com.riders.thelabback.data.model.user.User
import com.riders.thelabback.data.model.user.users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.registerLoginRoute() {
    routing {
        route("/login") {
            loginRoute()
        }
    }
}


fun Route.loginRoute() {
    post {
        val user = call.receive<User>()
        print("received : $user")

        if (user.email?.isBlank() == true || user.password?.isBlank() == true) {
            return@post call.respond(
                status = HttpStatusCode.BadRequest,
                message = ApiResponse(
                    "Email or password is empty. Please verify that you've entered a correct email or password",
                    HttpStatusCode.BadRequest.value
                )
            )
        }

        if (null == users.find { it.email == user.email }) {
            return@post call.respond(
                status = HttpStatusCode.NotFound,
                message = ApiResponse("No user found with this name ${user.email}", HttpStatusCode.NotFound.value)
            )
        }

        if (null == user.token) {
            call.respond(
                status = HttpStatusCode.NotFound,
                message = ApiResponse("password is incorrect", HttpStatusCode.NotFound.value)
            )
        } else {

            if (user.token == users.find { it.email == user.email }!!.token) {

                // User logging is okay
                println("User logging is okay")

                call.respond(
                    status = HttpStatusCode.OK,
                    message = ApiResponse(
                        "Login okay",
                        HttpStatusCode.OK.value,
                        users.find { it.email == user.email }!!.token
                    )
                )
            } else {
                call.respond(
                    status = HttpStatusCode.NotFound,
                    message = ApiResponse(
                        "Password is invalid",
                        HttpStatusCode.NotFound.value,
                        ""
                    )
                )
            }
        }
    }
}