package com.riders.thelabback

import com.riders.thelabback.configuration.configureMonitoring
import com.riders.thelabback.configuration.configureRouting
import com.riders.thelabback.configuration.configureSerialization
import com.riders.thelabback.core.logs.Timber
import com.riders.thelabback.core.logs.plant
import com.riders.thelabback.core.utils.users
import com.riders.thelabback.data.database.dao.UsersDao
import com.riders.thelabback.data.model.user.User
import com.riders.thelabback.data.model.user.Users
import com.riders.thelabback.di.configureDI
import com.riders.thelabback.di.initKoin
import com.toxicbakery.logging.Seedling
import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.plugin.KoinApplicationStarted
import org.koin.ktor.plugin.KoinApplicationStopPreparing
import org.koin.ktor.plugin.KoinApplicationStopped


fun main(args: Array<String>) {
    plant(Seedling())

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    println("main() | Hello World!")

    /*
     * Configuration: code vs configuration file
     * Running a Ktor application depends on the way you used to create a server - embeddedServer or EngineMain
     */
    // For EngineMain, Ktor loads its configuration from an external file that uses the HOCON or YAML format.
    // Using this approach, you can run a packaged application from a command line and override
    // the required server parameters by passing corresponding command-line arguments.
    EngineMain.main(args)

    // For embeddedServer, server parameters (such as a host address and port) are configured in code,
    // so you cannot change these parameters when running an application.
//    embeddedServer(Netty, host = "127.0.0.1", port = 8080, module = Application::module).start(wait = true)
}

@JvmOverloads
fun Application.module(testing: Boolean = true) {
    val env = environment.config.propertyOrNull("ktor.environment")?.getString() ?: "unspecified"
    val port = environment.config.propertyOrNull("ktor.deployment.port")?.getString() ?: "8080"

    if (testing) {
        // Log used port
        // log.info("Listening on port $port")
        Timber.tag(this.javaClass.simpleName).d(
            "Listening on port $port, (env : ${
                when (env) {
                    "dev" -> "Development"
                    "prod" -> "Production"
                    else -> "unspecified"
                }
            })"
        )
    }

    initKoin()

    configureDI()
    listenKtorEvents()

    configureMonitoring()
    configureSerialization()
    configureRouting()

    initUsers()
}

fun Application.listenKtorEvents() {
    Timber.d("listenKtorEvents() | Installing events. Suspends until provided...")


    // Install Ktor features
    monitor.subscribe(KoinApplicationStarted) {
        log.info("Koin started.")
    }

    monitor.subscribe(KoinApplicationStopPreparing) {
        log.info("Koin stopping...")
    }

    monitor.subscribe(KoinApplicationStopped) {
        log.info("Koin stopped.")
    }
}

/*

fun Application.installEvents() {
    Timber.d("installEvents() | Installing events. Suspends until provided...")

    // Suspends until provided
    dependencies {
        provide<Database> { AppModule.provideDatabase(databaseOptions) }
    }
}
*/
fun initUsers() {
    Timber.i("initUsers() | Initializing users...")

    val usersDao: UsersDao by inject(UsersDao::class.java)
    usersDao.apply { init(Users) }

    val firstUser: User = users.first()

    CoroutineScope(Dispatchers.IO).launch {
        usersDao
            .createUser(firstName = firstUser.firstName, firstUser.lastName)
            .also { insertStatement ->
                val id = insertStatement[Users.id]
                if (id > 0) {
                    firstUser.id = id
                }

                Timber.i("first user : ${firstUser}, inserted user : ${insertStatement[Users.id]} ")
            }
    }
}
