package com.example.smilie.screens.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class SettingsManager(initialDark: Boolean, initialFontSize: Float) {
    val isDark = mutableStateOf(initialDark)
    var fontSizeSlider: MutableState<Float> = mutableStateOf(initialFontSize)

    fun toggleDarkMode() {
        isDark.value = !isDark.value
    }
}