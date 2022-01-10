package com.riders.thelabback.data.model.user

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table


object Users : Table() {
    val id = varchar("id", 10) // Column<String>
    val gender = varchar("gender", 10) // Column<String>
    val firstName = varchar("firstName", length = 50) // Column<String>
    val lastName = varchar("lastName", length = 50) // Column<String>
    val email = varchar("email", length = 50) // Column<String>
    val password = varchar("password", length = 50) // Column<String>
    val phoneNumber = varchar("phoneNumber", length = 13) // Column<String>
    val dateOfBirth = varchar("dateOfBirth", length = 50) // Column<String>
    val isPremium = bool("isPremium") // Column<Boolean>
    val isCustomer = bool("isCustomer") // Column<Boolean>
    val isProvider = bool("isProvider") // Column<Boolean>
    val token = varchar("token", length = 50) // Column<String>

    override val primaryKey = PrimaryKey(id, name = "PK_User_ID") // name is optional here
}

@Serializable
data class User(
    var gender: String?,
    var firstName: String?,
    var lastName: String?,
    var email: String?,
    var password: String?,
    var phoneNumber: String?,
    var dateOfBirth: String?,
    var isPremium: Boolean = false,
    var isCustomer: Boolean = false,
    var isProvider: Boolean = false,
    var token: String?
) {
    var id: Int = 0

    constructor(id: Int, firstName: String, lastName: String) : this(
        firstName,
        lastName,
        null,
        null,
        null,
        null,
        null,
        false,
        false,
        false,
        ""
    ) {
        this.id = id
    }

    constructor(
        gender: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phoneNumber: String,
        dateOfBirth: String,
        isPremium: Boolean,
        isCustomer: Boolean,
        isProvider: Boolean
    ) : this(
        gender,
        firstName,
        lastName,
        email,
        password,
        phoneNumber,
        dateOfBirth,
        isPremium,
        isCustomer,
        isProvider,
        ""
    )

}
