package com.example.smilie.screens.settings

import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.view.SmilieViewModel
import com.example.smilie.ui.navigation.LOGIN_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val accountBackend: AccountBackend) : SmilieViewModel() {
    fun onSignOutClick(openAndPopUp: (String) -> Unit) {
        launchCatching {
            accountBackend.signOut()
            openAndPopUp(LOGIN_SCREEN)
        }
    }
}