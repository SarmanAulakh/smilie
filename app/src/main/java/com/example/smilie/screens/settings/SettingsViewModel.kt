package com.example.smilie.screens.settings

import com.example.smilie.model.service.AccountService
import com.example.smilie.screens.SmilieViewModel
import com.example.smilie.ui.components.navigation.LOGIN_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val accountService: AccountService) : SmilieViewModel() {
    fun onSignOutClick(openAndPopUp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            openAndPopUp(LOGIN_SCREEN)
        }
    }
}