package com.example.smilie.screens.login

import androidx.compose.runtime.mutableStateOf
import com.example.smilie.model.service.AccountService
import com.example.smilie.screens.SmilieViewModel
import com.example.smilie.ui.components.common.ext.isValidEmail
import com.example.smilie.ui.components.navigation.LOGIN_SCREEN
import com.example.smilie.ui.components.navigation.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val accountService: AccountService) : SmilieViewModel() {
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
            uiState.value.copy(message = "Invalid Email")
            return
        }

        if (password.isBlank()) {
            uiState.value.copy(message = "Invalid Password")
            return
        }

        launchCatching {
            accountService.authenticate(email, password)
            openAndPopUp(Settings.route)
        }
    }
}