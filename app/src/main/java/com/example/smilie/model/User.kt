package com.example.smilie.model

import com.google.gson.annotations.SerializedName

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
    val following: List<String>? = null,
    val show_notifications: Boolean = true,
    val fcmToken: String = ""
)
