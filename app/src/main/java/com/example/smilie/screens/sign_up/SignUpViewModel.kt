package com.example.smilie.screens.sign_up

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.smilie.model.service.AccountService
import com.example.smilie.screens.SmilieViewModel
import com.example.smilie.ui.components.common.ext.isValidEmail
import com.example.smilie.ui.components.common.ext.passwordMatches
import com.example.smilie.ui.components.navigation.Settings
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
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


        val users = db.collection("Users")


        launchCatching {

            var id = Random.nextInt(0, Int.MAX_VALUE)
            //TO:DO add some sort of checking to make sure values are unique

            Log.i("SignUpViewModel", "this is count:$id")
            val userMap = hashMapOf(
                "Username" to email,
                "id" to id
            )
            accountService.signUp(email, password)
            openAndPopUp(Settings.route)

            db.collection("Users").document().set(userMap)
        }
    }
}