package com.example.smilie.ui.components.navigation

import androidx.lifecycle.ViewModel
import com.example.smilie.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class BottomNavBarViewModel @Inject constructor(private val accountService: AccountService) : ViewModel() {
    val userExists = accountService.userExists
}