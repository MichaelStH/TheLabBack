package com.riders.thelabback.data.model.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val message: String,
    val code: Int,
    val token: String? = null
) {
    constructor(message: String, code: Int) : this(message, code, null)
}
