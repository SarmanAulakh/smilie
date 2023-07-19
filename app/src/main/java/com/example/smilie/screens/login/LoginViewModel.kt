package com.example.smilie.screens.login

import androidx.compose.runtime.mutableStateOf
import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.view.SmilieViewModel
import com.example.smilie.ui.components.common.ext.isValidEmail
import com.example.smilie.ui.navigation.Home
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val accountBackend: AccountBackend) : SmilieViewModel() {
    private var db = Firebase.firestore
    var uiState = mutableStateOf(LoginState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password
    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(openAndPopUp: (String) -> Unit) {
        if (!email.isValidEmail()) {
            uiState.value = uiState.value.copy(message = "Invalid Email")
            return
        }

        if (password.isBlank()) {
            uiState.value = uiState.value.copy(message = "Invalid Password")
            return
        }
        setLoading(true)
        launchCatching(onError = { setLoading(false) }) {
            accountBackend.authenticate(email, password)
            openAndPopUp(Home.route)
            setLoading(false)
        }
    }

    private fun setLoading(newVal: Boolean) {
        uiState.value = uiState.value.copy(loading = newVal)
    }
}