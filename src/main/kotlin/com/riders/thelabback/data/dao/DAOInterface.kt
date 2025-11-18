package com.riders.thelabback.data.dao

import com.riders.thelabback.data.model.user.User
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.io.Closeable

/**
 * Defines the contract for Data Access Object operations.
 * This interface provides methods for initializing the database schema and performing
 * CRUD (Create, Read, Update, Delete) operations on users.
 * It extends [Closeable] to ensure that database connections can be properly managed and released.
 */
interface DAOInterface : Closeable {
    /**
     * Initializes the database with the specified tables.
     * This should be called to create the database schema if it doesn't exist.
     * @param T The type of the table, which must extend [Table].
     * @param tables A vararg of table objects to be created in the database.
     */
    fun <T : Table> init(vararg tables: T)

    /**
     * Creates a new user in the database.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @return An [InsertStatement] which can be used to retrieve the generated ID of the new user.
     */
    fun createUser(firstName: String, lastName: String): InsertStatement<Number>

    /**
     * Retrieves all users from the database.
     * @return A [List] of all [User] objects.
     */
    fun getAllUsers(): List<User>

    /**
     * Retrieves a single user by their unique ID.
     * @param id The ID of the user to retrieve.
     * @return The [User] object if found, otherwise `null`.
     */
    fun getUser(id: Int): User?

    /**
     * Updates an existing user's information.
     * @param id The ID of the user to update.
     * @param firstName The new first name for the user.
     * @param lastName The new last name for the user.
     * @return The number of rows affected by the update operation.
     */
    fun updateUser(id: Int, firstName: String, lastName: String): Int

    /**
     * Deletes a user from the database.
     * @param id The ID of the user to delete.
     * @return The number of rows affected by the delete operation.
     */
    fun deleteUser(id: Int): Int
}