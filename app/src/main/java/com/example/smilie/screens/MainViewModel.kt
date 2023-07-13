package com.example.smilie.screens

import android.app.Application
import android.widget.Toast
import com.example.smilie.model.User
import com.example.smilie.model.service.AccountService
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val accountService: AccountService
    ) : SmilieViewModel() {

    private var db = Firebase.firestore
    var userData: User? = null

    init {
        getUserDetails()
    }

    fun getUserDetails() {
        db.collection("users")
            .document(accountService.currentUserId)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    userData = it.toObject<User>()
                } else {
                    Toast.makeText(
                        application,
                        "user data not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    application,
                    "Failed to get user data.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}