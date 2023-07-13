package com.example.smilie.screens.sign_up

import androidx.compose.runtime.mutableStateOf
import com.example.smilie.model.User
import com.example.smilie.model.UserTypes
import com.example.smilie.model.service.AccountService
import com.example.smilie.screens.SmilieViewModel
import com.example.smilie.ui.navigation.Home
import com.example.smilie.ui.navigation.USER_REGISTER_SCREEN
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.last
import javax.inject.Inject

data class UserRegisterState(
    val username: String = "",
    val userType: UserTypes = UserTypes.DEFAULT,
    val imageUrl: String = "https://firebasestorage.googleapis.com/v0/b/socialmedia-5f158.appspot.com/o/no-img.png?alt=media&token=119463d0-c5e9-4059-9805-1f4d7dbff3c0",
    val message: String = "",
    val loading: Boolean = false,
)

@HiltViewModel
class UserRegisterViewModel @Inject constructor(private val accountService: AccountService) : SmilieViewModel() {
    var uiState = mutableStateOf(UserRegisterState())
        private set

    private val username
        get() = uiState.value.username
    private val userType
        get() = uiState.value.userType
    private val imageUrl
        get() = uiState.value.imageUrl

    private var db = Firebase.firestore

    fun onUsernameChange(newValue: String) {
        uiState.value = uiState.value.copy(username = newValue)
    }

    fun onUserTypeChange(newValue: UserTypes) {
        uiState.value = uiState.value.copy(userType = newValue)
    }

    fun onImageChange(newValue: String) {
        uiState.value = uiState.value.copy(imageUrl = newValue)
    }

    fun onRegisterUserClick(openAndPopUp: (String) -> Unit) {
        val currentFirebaseUser = accountService.currentUser
        currentFirebaseUser?.let {
            val user = User(
                id = it.id,
                username = uiState.value.username,
                userType = uiState.value.userType,
                email = it.email,
                imageUrl = uiState.value.imageUrl,
            )

            // add user to db
            db.collection("users").document(it.id).set(user)
            openAndPopUp(Home.route)
        }
    }
}