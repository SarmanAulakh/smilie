package com.example.smilie.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.core.view.WindowCompat
import com.example.smilie.screens.settings.DarkModeManager

@Composable
fun SMILIETheme(
    darkModeManager: DarkModeManager = DarkModeManager(true),
    content: @Composable () -> Unit
) {
    val colorPalette = when {
        darkModeManager.isDark.value -> DarkThemeColors
        else -> LightThemeColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorPalette.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkModeManager.isDark.value
        }
    }

    MaterialTheme(
        colorScheme = colorPalette,
        typography = Typography,
        content = {
            ProvideTextStyle(
                value = TextStyle(color = colorPalette.onBackground),
                content = content
            )
        }
    )
}