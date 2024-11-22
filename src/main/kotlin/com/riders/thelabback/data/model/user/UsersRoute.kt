package com.riders.thelabback.data.model.user


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

val users = mutableListOf(
    User(
        "Michael",
        "Lawrence",
        "mike@test.fr",
        "test_mike",
        encodedHashedPassword(convertToSHA1("test_mike")!!)
    ),
    User(
        "Jane",
        "Doe",
        "janedoe@test.fr",
        "test",
        encodedHashedPassword(convertToSHA1("test")!!)
    ),
    User(
        "John",
        "Smith",
        "johnsmith@test.fr",
        "johnSmith_45",
        encodedHashedPassword(convertToSHA1("johnSmith_45")!!)
    )
)


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


fun Route.addUserRoute() {
    post {
        val user = call.receive<User>()
        print("received : $user \n")

        if (user.password.isNullOrBlank()) {
            println("ERROR - Password MUST NOT be null \n")

            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = UserResponse(
                    HttpStatusCode.Unauthorized.value,
                    " Password MUST NOT be null",
                    ""
                )
            )
        } else {

            var token: String? = ""

            try {
                // Convert to SHA-1
                val sha1hash: ByteArray? = convertToSHA1(user.password!!)

                if (null == sha1hash) {
                    println("Failed to convert to SHA-1")
                    return@post
                }

                // Encode hashed password
                token = encodedHashedPassword(sha1hash)

                println("generated token : $token \n")

                // Apply token to current user
                user.token = token

            } catch (exception: NoSuchAlgorithmException) {
                exception.printStackTrace()
            } catch (ex: UnsupportedEncodingException) {
                ex.printStackTrace()
            }

            // Add user to list (or database table)
            users.add(user)

            call.respond(
                status = HttpStatusCode.Created,
                message = UserResponse(
                    HttpStatusCode.Created.value,
                    "User saved",
                    token!!
                )
            )
        }

    }
}


fun Route.getUsersRoute() {
    get {
        if (users.isNotEmpty()) {
            call.respond(HttpStatusCode.OK, users)
        } else {
            call.respond(HttpStatusCode.NotFound, "No user found")
        }
    }
}

fun Route.getUserRoute() {
    get("{id}") {
        val id = call.parameters["id"]?.toInt() ?: return@get call.respondText(
            "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val user = users.find { it.id == id }
        user?.let { call.respond(HttpStatusCode.Found, it) } ?: return@get call.respond(
            HttpStatusCode.NotFound,
            "No user found with this id $id"
        )
    }
}

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


fun convertToSHA1(password: String): ByteArray? {
    try {
        // Convert to SHA-1
        val digest: MessageDigest = MessageDigest.getInstance("SHA-1")
        val textByteArray: ByteArray = "${password}_the_lab_data".toByteArray(charset("iso-8859-1"))

        digest.update(textByteArray, 0, textByteArray.size)

        return digest.digest()

    } catch (exception: NoSuchAlgorithmException) {
        exception.printStackTrace()
    } catch (ex: UnsupportedEncodingException) {
        ex.printStackTrace()
    }

    return null
}

fun encodedHashedPassword(sha1hash: ByteArray): String? {
    try {
        val sb = StringBuilder()
        for (b in sha1hash) {
            var halfByte: Int = b.toInt() ushr 4 and 0x0F
            var twoHalfs: Int = 0

            do {
                sb.append(
                    if (halfByte in 0..9) ('0'.code + halfByte).toChar() else ('0'.code + (halfByte + 10)).toChar()
                )
                halfByte = (b and 0x0F).toInt()
            } while (twoHalfs++ < 1)
        }
        return sb.toString()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return null
}