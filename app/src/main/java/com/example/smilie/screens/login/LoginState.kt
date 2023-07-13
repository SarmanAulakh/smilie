package com.example.smilie.screens.login

import com.example.smilie.model.User

data class LoginState(
    val email: String = "",
    val password: String = "",
    val user: User? = null,
    val message: String = "",
    val loading: Boolean = false,
)