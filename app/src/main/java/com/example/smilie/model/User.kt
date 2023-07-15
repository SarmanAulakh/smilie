package com.example.smilie.model

enum class UserTypes {
    DEFAULT,
    STUDENT,
    INFLUENCER,
}

data class User(
    val id: String = "",
    val username: String = "",
    val userType: UserTypes = UserTypes.DEFAULT,
    val email: String = "",
    val imageUrl: String = "",
    val bio: String = "",
    val followers: List<String>? = null,
)