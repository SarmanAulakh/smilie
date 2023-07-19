package com.example.smilie.model.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.smilie.model.User
import com.example.smilie.model.service.backend.UserBackend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val userBackend: UserBackend,
    private val followingIds: List<String>
) : SmilieViewModel() {
    val followingUsers: MutableState<List<User>> = mutableStateOf(ArrayList())

    init {
        viewModelScope.launch {
            followingUsers.value = userBackend.getByIds(followingIds)
        }
    }
}