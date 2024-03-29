package com.example.smilie.model.view

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.smilie.model.User
import com.example.smilie.model.UserTypes
import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.service.backend.UserBackend
import com.example.smilie.ui.navigation.Home
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val DEFAULT_PROFILE = "https://firebasestorage.googleapis.com/v0/b/socialmedia-5f158.appspot.com/o/no-img.png?alt=media&token=119463d0-c5e9-4059-9805-1f4d7dbff3c0"

data class UserRegisterState(
    val username: String = "",
    val userType: UserTypes = UserTypes.DEFAULT,
    val imageUri: Uri = Uri.parse(DEFAULT_PROFILE),
    val message: String = "",
    val loading: Boolean = false,
)

@HiltViewModel
class UserRegisterViewModel @Inject constructor(
    private val accountBackend: AccountBackend,
    private val userBackend: UserBackend
) : SmilieViewModel() {
    lateinit var storage: FirebaseStorage
    var uiState = mutableStateOf(UserRegisterState())
        private set

    private val username
        get() = uiState.value.username
    private val userType
        get() = uiState.value.userType
    private val imageUrl
        get() = uiState.value.imageUri

    private var db = Firebase.firestore

    fun onUsernameChange(newValue: String) {
        uiState.value = uiState.value.copy(username = newValue)
    }

    fun onUserTypeChange(newValue: UserTypes) {
        uiState.value = uiState.value.copy(userType = newValue)
    }

    fun onImageChange(newValue: Uri) {
        uiState.value = uiState.value.copy(imageUri = newValue)
    }

    fun onRegisterUserClick(openAndPopUp: (String) -> Unit) {
        val uri = uiState.value.imageUri
        val storage = Firebase.storage
        val storageRef = storage.reference
        val ref = storageRef.child("images/${uiState.value.username}:${uri.lastPathSegment}")

        // upload image if uri != default image
        if (uri.toString() == DEFAULT_PROFILE) {
            createUserDocument(openAndPopUp, DEFAULT_PROFILE)
        } else {
            // upload image and then create user
            ref.putFile(uri)
                .addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val fileDownloadUrl = downloadUrl.toString()
                        createUserDocument(openAndPopUp, fileDownloadUrl)
                    }.addOnFailureListener {
                        uiState.value = uiState.value.copy(message = it.message.toString())
                    }
                }
                .addOnFailureListener {
                    uiState.value = uiState.value.copy(message = it.message.toString())
                }
        }
    }

    private fun createUserDocument(openAndPopUp: (String) -> Unit, imageUrl: String) {
        val currentFirebaseUser = accountBackend.currentUser
        currentFirebaseUser?.let {
            viewModelScope.launch {
                val fcmToken = Firebase.messaging.token.await();
                val user = User(
                    id = it.id,
                    username = uiState.value.username,
                    userType = uiState.value.userType,
                    email = it.email,
                    imageUrl = imageUrl,
                    fcmToken = fcmToken
                )
                userBackend.createUser(user)
                openAndPopUp(Home.route)
            }
        }
    }
}