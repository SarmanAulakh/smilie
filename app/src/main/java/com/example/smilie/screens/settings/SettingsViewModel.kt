package com.example.smilie.screens.settings

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.smilie.model.Metric
import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.service.backend.MetricBackend
import com.example.smilie.model.service.backend.UserBackend
import com.example.smilie.model.view.SmilieViewModel
import com.example.smilie.ui.navigation.LOGIN_SCREEN
import com.example.smilie.ui.navigation.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserUpdateBody(var isNotificationOn: Boolean? = null, var followingUserId: String? = null)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val metricBackend: MetricBackend,
    private val userBackend: UserBackend,
    private val accountBackend: AccountBackend,

    ) : SmilieViewModel() {
    val isNotificationsOn = mutableStateOf(true)

    fun onSignOutClick(openAndPopUp: (String) -> Unit) {
        launchCatching {
            accountBackend.signOut()
            openAndPopUp(LOGIN_SCREEN)
        }
    }

    fun toggleNotificationsMode() {
        isNotificationsOn.value = !isNotificationsOn.value
        saveUser()
    }

    fun saveUser() {
        viewModelScope.launch {
            userBackend.updateUser(id=accountBackend.currentUserId, UserUpdateBody(isNotificationsOn.value))
        }
    }

    fun saveMetrics(openAndPopUp: (String) -> Unit, metrics: ArrayList<Metric>) {

        viewModelScope.launch {
            if(metricBackend.editMetrics(id=accountBackend.currentUserId, metrics=metrics)) {
                Log.d("SmilieDebug", "Edited successfully")
            } else {
                Log.d("SmilieDebug", "Failed to edit metrics")
            }
        }
        launchCatching {
            openAndPopUp(Settings.route)
        }
    }
}