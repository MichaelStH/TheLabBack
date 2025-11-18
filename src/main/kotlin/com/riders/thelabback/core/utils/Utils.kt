package com.riders.thelabback.core.utils

import com.riders.thelabback.core.logs.Timber
import com.riders.thelabback.core.utils.Utils.encodedHashedPassword
import java.security.MessageDigest

/**
 * A utility object providing helper functions, primarily for cryptographic operations.
 *
 * @warning The hashing functions in this utility object use SHA-1, which is considered
 * cryptographically weak and should not be used for new security-sensitive applications
 * like password hashing. Consider migrating to stronger algorithms like SHA-256,
 * or dedicated password hashing functions like bcrypt or Argon2.
 */
object Utils {

    /**
     * Computes the SHA-1 hash of a string after appending a hardcoded salt.
     *
     * This function appends `_the_lab_data` to the input string, converts it to a byte array
     * using the UTF-8 charset, and then computes the SHA-1 hash.
     *
     * @receiver The input [String] to be hashed.
     * @return The SHA-1 hash as a [ByteArray], or `null` if the SHA-1 algorithm is not available on the system.
     *
     * @see encodedHashedPassword
     */
    fun String.convertToSHA1(): ByteArray? = runCatching {
        // Using a stronger charset like UTF-8 is generally recommended over ISO_8859_1.
        // The salt is hardcoded, which is a security risk. It should ideally be loaded from a secure configuration.
        val textToHash = "${this}_the_lab_data"
        val digest = MessageDigest.getInstance("SHA-1")
        digest.digest(textToHash.toByteArray(Charsets.UTF_8))
    }
        .onFailure { exception ->
            // In a real application, use a proper logging framework instead of printStackTrace().
            exception.printStackTrace()
            Timber.e("convertToSHA1() | Error caught with message : ${exception.message} (class : ${exception.javaClass.canonicalName})")
        }
        .getOrNull()

    /**
     * Converts a byte array into its hexadecimal string representation.
     *
     * This is typically used to create a human-readable string from a cryptographic hash.
     *
     * A more idiomatic and efficient way to perform this conversion in Kotlin:
     * ```kotlin
     * fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
     * ```
     *
     * @receiver The [ByteArray] (e.g., a hash) to be encoded.
     * @return The hexadecimal [String] representation of the byte array.
     */
    fun ByteArray.encodedHashedPassword(): String = joinToString("") { byte -> "%02x".format(byte) }
}