package com.riders.thelabback.data.database.dao

import com.riders.thelabback.core.logs.Timber
import com.riders.thelabback.data.model.user.User
import com.riders.thelabback.data.model.user.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction

class UsersDao(private val db: Database) : DAOInterface {

    override fun <T : Table> init(vararg tables: T) = transaction(db) {
        Timber.d("init() | Initializing database, with ${tables.size} table(s) ...")

        // Enable logging : print sql to std-out
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(*tables)
    }

    override suspend fun createUser(
        firstName: String,
        lastName: String
    ): InsertStatement<Number> = withContext(Dispatchers.IO) {
        transaction(db) {
            Users.insert { insertStatement ->
                insertStatement[Users.firstName] = firstName
                insertStatement[Users.lastName] = lastName
                insertStatement[Users.email] = ""
                insertStatement[Users.password] = ""
                insertStatement[Users.token] = ""
            }
        }
    }


    override suspend fun getUser(id: Int): User? = withContext(Dispatchers.IO) {
        transaction(db) {
            Users.selectAll()
                .where { Users.id eq id }
                .map { User(id = it[Users.id], firstName = it[Users.firstName], lastName = it[Users.lastName]) }
                .singleOrNull()
        }
    }


    override suspend fun getAllUsers(): List<User> = withContext(Dispatchers.IO) {
        transaction(db) {
            Users
                .selectAll()
                .map { User(id = it[Users.id], firstName = it[Users.firstName], lastName = it[Users.lastName]) }
        }
    }


    override suspend fun updateUser(
        id: Int,
        firstName: String,
        lastName: String
    ): Int = withContext(Dispatchers.IO) {
        transaction(db) {
            Users.update({ Users.id eq id }) { updateStatement ->
                updateStatement[Users.firstName] = firstName
                updateStatement[Users.firstName] = lastName
            }
        }
    }

    override suspend fun deleteUser(id: Int): Int = withContext(Dispatchers.IO) {
        transaction(db) {
            Users.deleteWhere { Users.id eq id }
        }
    }

    override fun close() {
        // Ignored
    }
}