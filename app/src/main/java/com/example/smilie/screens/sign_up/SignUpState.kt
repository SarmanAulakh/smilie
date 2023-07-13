package com.example.smilie.screens.sign_up

import com.example.smilie.model.UserTypes

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val message: String = "",
    val loading: Boolean = false,
)

