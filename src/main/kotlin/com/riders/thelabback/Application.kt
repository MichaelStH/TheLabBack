package com.riders.thelabback

import com.riders.thelabback.configuration.configureMonitoring
import com.riders.thelabback.configuration.configureRouting
import com.riders.thelabback.configuration.configureSerialization
import com.riders.thelabback.core.logs.Timber
import com.riders.thelabback.core.logs.plant
import com.riders.thelabback.data.dao.UsersDao
import com.riders.thelabback.data.model.user.User
import com.riders.thelabback.data.model.user.Users
import com.riders.thelabback.data.model.user.users
import com.toxicbakery.logging.Seedling
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>): Unit {
    plant(Seedling())

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    Timber.d("main() | Hello World!")

    connectDatabase()

    EngineMain.main(args)
}

@JvmOverloads
fun Application.module(testing: Boolean = true) {
    val port = environment.config.propertyOrNull("ktor.deployment.port")?.getString() ?: "8080"

    if (testing) {
        // Log used port
        Timber.tag(this.javaClass.simpleName).d("Listening on port $port")
    }

    configureMonitoring()
    configureSerialization()
    configureRouting()
}

fun connectDatabase() {
    Timber.i("connectDatabase() | Connecting to database...")

    val databaseOptions = listOf("jdbc:h2:mem:test", "DB_CLOSE_DELAY=-1", "MODE=LEGACY").joinToString(";")
    val database = Database.connect(url = databaseOptions, driver = "org.h2.Driver")
    val dao = UsersDao(database).apply { init(Users) }

    val firstUser: User = users.first()
    dao
        .createUser(firstName = firstUser.firstName, firstUser.lastName)
        .also { insertStatement ->
            val id = insertStatement[Users.id]
            if (id > 0) {
                firstUser.id = id
            }

            Timber.i("first user : ${firstUser}, inserted user : ${insertStatement[Users.id]} ")
        }
}