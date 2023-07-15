package com.example.smilie.model.service

import com.example.smilie.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

// CODE REF: https://github.com/FirebaseExtended/make-it-so-android/blob/main/start/app/src/main/java/com/example/makeitso/model/service/AccountService.kt

interface AccountService {
    val currentUserId: String
    val userExists: Boolean
    // https://developer.android.com/kotlin/flow
    val currentUser: User?

    suspend fun authenticate(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun signOut()
}