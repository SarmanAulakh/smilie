package com.example.smilie.model.service.backend

import com.example.smilie.model.User
import com.example.smilie.screens.settings.UserUpdateBody

interface UserBackend {

    suspend fun createUser(user: User): User?

    suspend fun updateUser(id:String, userUpdateBody: UserUpdateBody): User?

    suspend fun getById(id: String): User?

    suspend fun getByIds(ids: List<String>): List<User>

    suspend fun getAll(): ArrayList<User>?
}