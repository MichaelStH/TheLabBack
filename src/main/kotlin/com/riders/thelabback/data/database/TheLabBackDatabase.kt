package com.riders.thelabback.data.database

import com.riders.thelabback.core.logs.Timber
import com.riders.thelabback.core.utils.Constants
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database


class TheLabBackDatabase(val connectionUrl: String) : AutoCloseable {

    fun get(): Database = Database.connect(url = connectionUrl, driver = "org.h2.Driver")

    override fun close() {
        Timber.e("close() | Close connections, release resources...")
        // Close connections, release resources
        closeDatabase()
    }

    fun closeDatabase(application: Application? = null) {
        if (null == application) {
            get().connector().close()
            return
        }

        // Gracefully close the database connection when the application stops
        application.environment.monitor.subscribe(ApplicationStopping) {
            get().connector().close()
        }
    }

    companion object {

        private var instance: TheLabBackDatabase? = null

        @Synchronized
        fun getInstance(
            connectionUrl: String
        ): TheLabBackDatabase = instance ?: synchronized(this) {
            instance ?: TheLabBackDatabase(connectionUrl).also { labBackDatabase ->
                instance = labBackDatabase
            }
        }
    }
}


fun Application.closeDatabase() {
    // Gracefully close the database connection when the application stops
    TheLabBackDatabase.getInstance(Constants.databaseOptions).closeDatabase(this)
}