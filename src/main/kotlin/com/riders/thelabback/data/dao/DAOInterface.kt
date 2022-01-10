package com.riders.thelabback.data.dao


import com.riders.thelabback.data.model.user.User
import java.io.Closeable

interface DAOInterface : Closeable {
    fun init()
    fun createUser(title: String, description: String)
    fun updateUser(id: Int, title: String, description: String)
    fun deleteUser(id: Int)
    fun getUser(id: Int): User?
    fun getAllUsers(): List<User>
}