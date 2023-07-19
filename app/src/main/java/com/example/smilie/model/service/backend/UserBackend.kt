package com.example.smilie.model.service.backend

import com.example.smilie.model.User

interface UserBackend {
    suspend fun getById(id: String): User?

    suspend fun getByIds(ids: List<String>): List<User>
}