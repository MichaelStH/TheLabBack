package com.riders.thelabback.data.model.user

import com.riders.thelabback.core.logs.Timber
import com.riders.thelabback.core.utils.Utils.convertToSHA1
import com.riders.thelabback.core.utils.Utils.encodedHashedPassword
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * A temporary in-memory list of users for demonstration purposes.
 * In a production environment, this should be replaced with a persistent data source
 * like a database accessed via a DAO.
 *
 * @warning This list stores user passwords as hashes in the `token` field for demonstration,
 * which is not a recommended practice. The `password` field itself contains plaintext,
 * which is a major security risk. This is for prototyping only.
 */
val users: MutableList<User> = mutableListOf(
    User(
        "Michael",
        "Lawrence",
        "mike@test.fr",
        // The plaintext password should not be stored. Here it's hashed and put in the token field.
        "test_mike".convertToSHA1()!!.encodedHashedPassword(),
        "test_mike".convertToSHA1()!!.encodedHashedPassword()
    ),
    User(
        "Jane",
        "Doe",
        "janedoe@test.fr",
        "test",
        "test".convertToSHA1()!!.encodedHashedPassword() // Mismatch: password is "test", token is hash of "test"
    ),
    User(
        "John",
        "Smith",
        "johnsmith@test.fr",
        "johnSmith_45",
        "johnSmith_45".convertToSHA1()!!.encodedHashedPassword()
    )
)

/**
 * Registers all user-related routes under the `/users` path.
 * This function acts as a container for organizing all endpoints that manage users.
 * @receiver The Ktor [Application] to which the routes will be added.
 */
fun Application.registerUsersRoute() {
    routing {
        route("/users") {
            addUserRoute()
            getUsersRoute()
            getUserRoute()
            deleteUserRoute()
        }
    }
}

/**
 * Defines the `POST /users` route for creating a new user.
 * It receives a [User] object, validates the password, hashes it, and adds the user
 * to the in-memory list.
 *
 * @receiver The Ktor [Route] to which this endpoint will be added.
 */
fun Route.addUserRoute() {
    post {
        val userRequest = call.receive<User>()
        Timber.i("Received user creation request: $userRequest")

        val password = userRequest.password
        if (password.isNullOrBlank()) {
            Timber.w("User creation failed: Password was null or blank.")
            return@post call.respond(
                status = HttpStatusCode.Unauthorized,
                message = UserResponse(
                    HttpStatusCode.Unauthorized.value,
                    "Password must not be empty.",
                    ""
                )
            )
        }

        // Hash the password for storage. The plaintext password should never be stored.
        val hashedPassword = password.convertToSHA1()?.encodedHashedPassword()

        if (hashedPassword == null) {
            Timber.e("Password hashing failed. SHA-1 algorithm might not be available.")
            return@post call.respond(
                status = HttpStatusCode.InternalServerError,
                message = UserResponse(
                    HttpStatusCode.InternalServerError.value,
                    "Failed to process user registration due to a server error.",
                    ""
                )
            )
        }

        // Create a new User object for storage, replacing the plaintext password with the hash.
        // The 'token' field should be used for session tokens (e.g., JWT), not the password hash.
        val newUser = userRequest.copy(
            password = hashedPassword,
            token = "" // A real session token would be generated upon login, not registration.
        )

        // Add user to list (or database table)
        users.add(newUser)
        Timber.i("User successfully created and stored: $newUser")

        call.respond(
            status = HttpStatusCode.Created,
            message = UserResponse(
                HttpStatusCode.Created.value,
                "User created successfully.",
                // Do not send the password hash back. A session token would go here after login.
                ""
            )
        )
    }
}

/**
 * Defines the `GET /users` route for retrieving all users.
 *
 * @receiver The Ktor [Route] to which this endpoint will be added.
 * @warning This implementation returns all user data, including hashed passwords.
 * In a production application, you must filter out sensitive data or use a DTO (Data Transfer Object).
 */
fun Route.getUsersRoute() {
    get("/") {
        if (users.isEmpty()) {
            call.respond(HttpStatusCode.NotFound, "No user found")
        } else {
            // SECURITY: Never send the full user object with password/token data to the client.
            // This should be mapped to a public-facing data class.
            call.respond(HttpStatusCode.OK, users)
        }
    }
}

/**
 * Defines the `GET /users/{id}` route for retrieving a single user by their ID.
 *
 * @receiver The Ktor [Route] to which this endpoint will be added.
 * @warning As with `getUsersRoute`, this sends sensitive data and should be refactored to use a DTO.
 */
fun Route.getUserRoute() {
    get("{id}") {
        val id = call.parameters["id"]?.toInt() ?: return@get call.respondText(
            "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )

        val user = users.find { it.id == id }

        if (null == user) {
            call.respond(HttpStatusCode.NotFound, "No user found with this id $id")
        } else {
            // SECURITY: Map to a DTO before sending.
            call.respond(HttpStatusCode.OK, user)
        }
    }
}

/**
 * Defines the `DELETE /users/{id}` route for deleting a user by their ID.
 * @receiver The Ktor [Route] to which this endpoint will be added.
 */
fun Route.deleteUserRoute() {
    delete("{id}") {
        val id =
            call.parameters["id"]?.toInt() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "User id required"
            )

        if (users.removeIf { it.id == id }) {
            call.respond(HttpStatusCode.Accepted, "User removed successfully")
        } else {
            call.respond(HttpStatusCode.NotFound, "No user found with this id $id")
        }
    }
}