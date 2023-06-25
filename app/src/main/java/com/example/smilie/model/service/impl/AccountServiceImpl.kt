package com.example.smilie.model.service.impl

import com.example.smilie.model.User
import com.example.smilie.model.service.AccountService
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
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

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid, it.displayName, it.email) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    // suspend = function can be blocking, only calling them from a coroutine
    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signUp(email: String, password: String) {
        println("$email: $password")
        auth.createUserWithEmailAndPassword(email, password).await()
//        val credential = EmailAuthProvider.getCredential(email, password)
//        auth.currentUser!!.linkWithCredential(credential).await()
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}