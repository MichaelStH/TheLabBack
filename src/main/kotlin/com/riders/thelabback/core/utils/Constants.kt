package com.riders.thelabback.core.utils

import com.riders.thelabback.core.utils.Utils.convertToSHA1
import com.riders.thelabback.core.utils.Utils.encodedHashedPassword
import com.riders.thelabback.data.model.user.User

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

object Constants {

    val databaseOptions = listOf("jdbc:h2:mem:test", "DB_CLOSE_DELAY=-1", "MODE=LEGACY").joinToString(";")

}