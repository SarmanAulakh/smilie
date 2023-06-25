package com.example.smilie.screens.sign_up

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val message: String = ""
)