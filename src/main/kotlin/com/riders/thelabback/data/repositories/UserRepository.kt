package com.riders.thelabback.data.repositories

import com.riders.thelabback.data.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUsers() : Flow<List<User>?>
}