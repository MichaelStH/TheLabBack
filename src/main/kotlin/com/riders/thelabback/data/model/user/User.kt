package com.riders.thelabback.data.model.user

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

/**
 * Defines the maximum length for variable-length character (VARCHAR) columns that require a larger size,
 * such as for email addresses.
 */
const val MAX_VARCHAR_LENGTH: Int = 128

/**
 * Represents the `users` table schema using the Exposed DSL.
 * This object defines the columns and primary key for the user table in the database.
 */
object Users : Table("users") {
    /** The unique identifier for the user, auto-incrementing. */
    val id = integer(name = "id").autoIncrement()

    /** The user's first name. */
    val firstName = varchar(name = "firstName", length = 50)

    /** The user's last name. */
    val lastName = varchar(name = "lastName", length = 50)

    /** The user's unique email address. */
    val email = varchar(name = "email", length = MAX_VARCHAR_LENGTH)

    /**
     * The user's hashed password.
     * @warning The current length of 50 is too short for modern, secure hashing algorithms like
     * Argon2 or bcrypt. For example, a bcrypt hash is typically 60 characters.
     * The length should be increased to at least 60, or preferably 255, to accommodate secure hashes.
     */
    val password = varchar(name = "password", length = 50)

    /**
     * A security or session token associated with the user.
     * @warning The length of 50 may be insufficient for certain token types, like JWTs,
     * which can be much longer. Consider increasing the length if longer tokens will be stored.
     */
    val token = varchar(name = "token", length = 50)

    /** Defines the primary key for the `users` table, which is the `id` column. */
    override val primaryKey = PrimaryKey(firstColumn = id, name = "PK_User_ID")
}

/**
 * Represents a user entity. This data class is used for serialization (e.g., to/from JSON)
 * and as a data transfer object within the application.
 *
 * It is recommended to make properties immutable (`val`) and non-nullable where possible,
 * and to include `id` in the primary constructor for a more robust data model.
 *
 * @property id The unique identifier of the user. Defaults to 0 for new users.
 * @property firstName The user's first name.
 * @property lastName The user's last name.
 * @property email The user's email address.
 * @property password The user's hashed password. Should not be exposed in API responses.
 * @property token A security or session token.
 */
@Serializable
data class User(
    val firstName: String,
    val lastName: String,
    val email: String? = null,
    val password: String? = null,
    var token: String? = null
) {
    /**
     * The unique identifier for the user.
     * It is declared outside the primary constructor, which is unconventional for a data class.
     * Consider moving it into the primary constructor.
     */
    var id: Int = 0

    constructor(id: Int, firstName: String, lastName: String) : this(
        firstName,
        lastName,
        null,
        null,
        ""
    ) {
        this.id = id
    }

    /**
     * Secondary constructor for creating a new user instance before it's saved to the database.
     * This is typically used when a user registers. The token is initialized to an empty string.
     * The `id` will be the default value (0) until assigned by the database.
     */
    constructor(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) : this(
        firstName,
        lastName,
        email,
        password,
        ""
    )

    /**
     * Note: This manual implementation of `equals` is redundant for a `data class`,
     * as the compiler automatically generates it based on the properties in the primary constructor.
     * This can be safely removed.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (token != other.token) return false

        return true
    }

    /**
     * Note: This manual implementation of `hashCode` is redundant for a `data class`,
     * as the compiler automatically generates it based on the properties in the primary constructor.
     * This can be safely removed.
     */
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (firstName?.hashCode() ?: 0)
        result = 31 * result + (lastName?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (password?.hashCode() ?: 0)
        result = 31 * result + (token?.hashCode() ?: 0)
        return result
    }

    /**
     * Note: This manual implementation of `toString` is redundant for a `data class`,
     * as the compiler automatically generates it based on the properties in the primary constructor.
     * This can be safely removed.
     */
    override fun toString(): String {
        return "User(id=$id, firstName=$firstName, lastName=$lastName, email=$email, password=$password, token=$token)"
    }
}
