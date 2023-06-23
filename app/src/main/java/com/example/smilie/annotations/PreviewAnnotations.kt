package com.example.smilie.annotations

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

// REFERENCE: https://www.youtube.com/watch?v=8XJfLaAOxD0&t=1s

@Preview(
    name = "Dark mode",
    group = "UI mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true
)
@Preview(
    name = "Light mode",
    group = "UI mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true
)
annotation class DarkLightPreviews