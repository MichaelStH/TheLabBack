package com.riders.thelabback.data.repositories

import com.riders.thelabback.core.logs.Timber
import com.riders.thelabback.data.database.dao.UsersDao
import com.riders.thelabback.data.model.user.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class UserRepositoryImpl(val usersDao: UsersDao) : UserRepository {

    init {
        Timber.tag(UserRepositoryImpl::class.java.simpleName).d("init() | users dao value : $usersDao")
    }

    override suspend fun getUsers(): Flow<List<User>?> = callbackFlow {
        trySend(usersDao.getAllUsers())

        awaitClose {
            Timber.e("getUsers() | callbackFlow.awaitClose()")
        }
    }
        .distinctUntilChanged()
}