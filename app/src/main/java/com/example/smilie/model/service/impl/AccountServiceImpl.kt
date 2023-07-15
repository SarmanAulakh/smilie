package com.example.smilie.model.service.impl

import com.example.smilie.model.User
import com.example.smilie.model.service.AccountService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// CODE REFERENCE: https://github.com/FirebaseExtended/make-it-so-android/blob/main/start/app/src/main/java/com/example/makeitso/model/service/impl/AccountServiceImpl.kt

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth): AccountService {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val userExists: Boolean
        get() = auth.currentUser != null

    override val currentUser: User?
        get() = auth.currentUser?.let { User(id=it.uid, email=it.email.toString()) }

    // suspend = function can be blocking, only calling them from a coroutine
    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password) .await()
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}