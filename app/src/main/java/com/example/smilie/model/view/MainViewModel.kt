package com.example.smilie.model.view

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.smilie.model.User
import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.service.backend.UserBackend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltViewModel
class MainViewModel @Inject constructor(
    private val accountBackend: AccountBackend,
    private val userBackend: UserBackend
    ) : SmilieViewModel() {

    var userData: User? = null

    init {
        viewModelScope.launch {
            userData = userBackend.getById(accountBackend.currentUserId)
        }
    }
}