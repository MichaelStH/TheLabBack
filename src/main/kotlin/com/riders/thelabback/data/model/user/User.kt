package com.riders.thelabback.data.model.user

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table


object Users : Table() {
    val id = varchar("id", 10) // Column<String>
    val firstName = varchar("firstName", length = 50) // Column<String>
    val lastName = varchar("lastName", length = 50) // Column<String>
    val email = varchar("email", length = 50) // Column<String>
    val password = varchar("password", length = 50) // Column<String>
    val token = varchar("token", length = 50) // Column<String>

    override val primaryKey = PrimaryKey(id, name = "PK_User_ID") // name is optional here
}

@Serializable
data class User(
    var firstName: String?,
    var lastName: String?,
    var email: String?,
    var password: String?,
    var token: String?
) {
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
}
