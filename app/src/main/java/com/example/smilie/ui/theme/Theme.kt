package com.example.smilie.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.core.view.WindowCompat
import com.example.smilie.screens.settings.SettingsManager

@Composable
fun SMILIETheme(
    settingsManager: SettingsManager = SettingsManager(true, 2f),
    content: @Composable () -> Unit
) {
    val colorPalette = when {
        settingsManager.isDark.value -> DarkThemeColors
        else -> LightThemeColors
    }

    val typography0 = ResizeableTypography(-4)
    val typography1 = ResizeableTypography(-2)
    val typography2 = ResizeableTypography(0)
    val typography3 = ResizeableTypography(2)
    val typography4 = ResizeableTypography(4)

    val typography = when (settingsManager.fontSizeSlider.value) {
        0f -> typography0.typography
        1f -> typography1.typography
        2f -> typography2.typography
        3f -> typography3.typography
        else -> typography4.typography
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorPalette.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = settingsManager.isDark.value
        }
    }

    MaterialTheme(
        colorScheme = colorPalette,
        typography = typography,
        content = {
            ProvideTextStyle(
                value = TextStyle(color = colorPalette.onBackground),
                content = content
            )
        }
    )
}