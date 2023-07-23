package com.example.smilie.model.view

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.smilie.model.User
import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.service.backend.UserBackend
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val accountBackend: AccountBackend,
    private val userBackend: UserBackend,
    private val auth: FirebaseAuth
    ) : SmilieViewModel() {

    var userData: MutableState<User?> = mutableStateOf(null)
    var userIdToken: MutableState<String?> = mutableStateOf(null)

    init {
        getUser()
        getUserIdToken()
    }

    fun getUser(userId: String = accountBackend.currentUserId) {
        viewModelScope.launch {
            Log.d("SmilieDebug", "Current UserId: $userId")
            userData.value = userBackend.getById(accountBackend.currentUserId)
        }
    }

    fun getUserIdToken() {
        val idTokenTask = auth.currentUser?.getIdToken(true)

        if (idTokenTask == null) {
            userIdToken.value = null
            return
        }

        idTokenTask!!.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("SmilieDebug", "userIdToken: ${task.result.token}")

                userIdToken.value = task.result.token
            } else {
                userIdToken.value = null
            }
        }
    }
}