package com.example.smilie.model.view

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.smilie.model.User
import com.example.smilie.model.Metric
import com.example.smilie.model.service.backend.MetricBackend
import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.service.backend.UserBackend
import com.example.smilie.screens.settings.UserUpdateBody
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val accountBackend: AccountBackend,
    private val metricBackend: MetricBackend,
    private val userBackend: UserBackend,
    private val auth: FirebaseAuth
    ) : SmilieViewModel() {
    var metricData: MutableState<ArrayList<Metric>?> = mutableStateOf(null)
    fun addFollowing(userId: String) {
        viewModelScope.launch {
            userBackend.updateUser(id=accountBackend.currentUserId, UserUpdateBody(followingUserId = userId))
        }
    }

    fun getMetrics(userId: String = accountBackend.currentUserId) {
        viewModelScope.launch {
            Log.d("SmilieDebug", "Current UserId: $userId")
            metricData.value = metricBackend.getMetricsById(accountBackend.currentUserId)
        }
    }
}