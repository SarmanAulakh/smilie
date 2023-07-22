package com.example.smilie.model.view

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.smilie.model.User
import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.service.backend.UserBackend
import com.example.smilie.ui.navigation.Profile
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
    val currentlyViewingUser: MutableState<User?> = mutableStateOf(null)

    init {
        updateCurrentlyViewingUser(null)
    }

    fun updateCurrentlyViewingUser(userId: String?) {
        val searchUserId = userId ?: accountBackend.currentUserId

        viewModelScope.launch {
            val user = userBackend.getById(searchUserId)

            if (user != null) {
                currentlyViewingUser.value = user

                if (user.following != null) {
                    followingUsers.value = userBackend.getByIds(user.following)
                } else {
                    followingUsers.value = ArrayList()
                }
            }
        }
    }

    fun onFriendClick(openAndPopUp: (String) -> Unit, userId: String) {
        launchCatching {
            openAndPopUp(Profile.route + "?userId=$userId")
        }
    }
}