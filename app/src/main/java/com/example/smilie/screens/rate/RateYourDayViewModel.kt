package com.example.smilie.screens.rate

import androidx.compose.material3.Text
import com.example.smilie.model.view.SmilieViewModel
import com.example.smilie.ui.navigation.ADD_METRICS_SCREEN
import com.example.smilie.ui.navigation.REMOVE_METRICS_SCREEN
import com.example.smilie.ui.navigation.RateYourDay
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RateYourDayViewModel @Inject constructor() : SmilieViewModel() {
    fun onRemoveClick(openAndPopUp: (String) -> Unit) {
        launchCatching {
            openAndPopUp(REMOVE_METRICS_SCREEN)
        }
    }
    fun onAddClick(openAndPopUp: (String) -> Unit) {
        launchCatching {
            openAndPopUp(ADD_METRICS_SCREEN)
        }
    }
    fun onEditComplete(openAndPopUp: (String) -> Unit) {
        launchCatching {
            openAndPopUp(RateYourDay.route)
        }
    }
}