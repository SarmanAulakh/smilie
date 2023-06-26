package com.example.smilie.screens.sign_up

import androidx.compose.runtime.mutableStateOf
import com.example.smilie.model.service.AccountService
import com.example.smilie.screens.SmilieViewModel
import com.example.smilie.ui.components.common.ext.isValidEmail
import com.example.smilie.ui.components.common.ext.passwordMatches
import com.example.smilie.ui.components.navigation.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
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

        if (!password.passwordMatches(repeatPassword)) {
            uiState.value = uiState.value.copy(message = "Password Mismatch")
            return
        }

        launchCatching {
            accountService.signUp(email, password)
            openAndPopUp(Settings.route)
        }
    }
}