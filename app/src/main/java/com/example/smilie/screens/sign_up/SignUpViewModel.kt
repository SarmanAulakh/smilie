package com.example.smilie.screens.sign_up

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.smilie.model.User
import com.example.smilie.model.UserTypes
import com.example.smilie.model.service.AccountService
import com.example.smilie.screens.SmilieViewModel
import com.example.smilie.ui.components.common.ext.isValidEmail
import com.example.smilie.ui.components.common.ext.passwordMatches
import com.example.smilie.ui.navigation.Home
import com.example.smilie.ui.navigation.USER_REGISTER_SCREEN
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SignUpViewModel @Inject constructor(private val accountService: AccountService) : SmilieViewModel() {
    var uiState = mutableStateOf(SignUpState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password
    private val repeatPassword
        get() = uiState.value.repeatPassword

    private var db = Firebase.firestore

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick(openAndPopUp: (String) -> Unit) {
        if (!email.isValidEmail()) {
            uiState.value = uiState.value.copy(message = "Invalid Email")
            return
        }

        if (password.isBlank()) {
            uiState.value = uiState.value.copy(message = "Invalid Password")
            return
        }

        if (!password.passwordMatches(repeatPassword)) {
            uiState.value = uiState.value.copy(message = "Password Mismatch")
            return
        }

        uiState.value = uiState.value.copy(loading = true)
        launchCatching(onError = {uiState.value = uiState.value.copy(loading = false)}) {
            // create user auth
            accountService.signUp(email, password)
            openAndPopUp(USER_REGISTER_SCREEN)
            uiState.value = uiState.value.copy(loading = false)
        }
    }
}