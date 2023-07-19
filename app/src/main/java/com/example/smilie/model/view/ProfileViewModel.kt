package com.example.smilie.model.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.smilie.model.User
import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.service.backend.UserBackend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val accountBackend: AccountBackend,
    private val userBackend: UserBackend,
) : SmilieViewModel() {
    val followingUsers: MutableState<List<User>> = mutableStateOf(ArrayList())

    init {
        viewModelScope.launch {
            val user = userBackend.getById(accountBackend.currentUserId)

            if (user !== null && user.following !== null) {

                followingUsers.value = userBackend.getByIds(user.following)
            }
        }
    }
}