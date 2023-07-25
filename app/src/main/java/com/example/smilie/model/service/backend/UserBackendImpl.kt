package com.example.smilie.model.service.backend

import android.util.Log
import android.widget.Toast
import com.example.smilie.SmilieHiltApp
import com.example.smilie.model.User
import com.example.smilie.model.service.UserService
import java.net.SocketTimeoutException

class UserBackendImpl(
    private val application: SmilieHiltApp,
    private val userService: UserService,
): UserBackend {

    override suspend fun createUser(user: User): User? {
        return userService.create(user);
    }

    override suspend fun getById(id: String): User? {
        if (id == "") {
            return null
        }
        val user = userService.get(id)

        if (user == null) {
            Toast.makeText(
                application,
                "User data not found",
                Toast.LENGTH_SHORT
            ).show()
        }

        return user
    }


    override suspend fun getByIds(ids: List<String>): List<User> {
        var users = ArrayList<User>()
        for (id in ids) {
            val user = userService.get(id)

            if (user !== null) {
                users.add(user)
            }
        }

        return users
    }
}