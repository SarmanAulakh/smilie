package com.example.smilie.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

data class SettingsItem(val name: String, var isEnabled: Boolean)

@Composable
fun SettingsScreen(
    openAndPopUp: (String) -> Unit,
    darkModeManager: DarkModeManager,
    fontSizeManager: FontSizeManager,
    settingViewModel: SignUpViewModel = hiltViewModel()
) {
    val settingsList: MutableList<SettingsItem> = remember {
        mutableStateListOf(
            SettingsItem("metric1", true),
            SettingsItem("metric2",true),
            SettingsItem("metric3", false)
        )
    }
    // Declaring a boolean value for storing checked state
    val mCheckedState: MutableList<Boolean> = remember{ mutableStateListOf()}
    settingsList.forEach{
        mCheckedState.add(it.isEnabled)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "SETTINGS",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.Bold,
            )
            Row() {
                Button(onClick = { settingViewModel.onSignOutClick(openAndPopUp) }) {
                    Text(text = "Sign Out")
                }
            }
            DarkModeSwitch(darkModeManager)
            TextSizeSlider(fontSizeManager)


            Spacer(Modifier.height(30.dp))
            Text(
                text = "Metrics Privacy",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
            )
            MetricPrivacy(settingsList, mCheckedState)
        }
    }
}

@Composable
fun MetricPrivacy(settingsList: List<SettingsItem>, mCheckedState: MutableList<Boolean>) {
    settingsList.forEachIndexed {index, item ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = item.name)
            Switch(
                checked = mCheckedState[index],
                onCheckedChange = { mCheckedState[index] = it })
        }
    }
}

class FontSizeManager(initialFontSize: Float) {
    var fontSizeSlider: MutableState<Float> = mutableStateOf(initialFontSize)
}

@Composable
fun TextSizeSlider(fontSizeManager: FontSizeManager) {
    Text(text = "Text Size")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Slider(
            value = fontSizeManager.fontSizeSlider.value,
            onValueChange = { fontSizeManager.fontSizeSlider.value = it },
            valueRange = 0f..4f,
            steps = 3
        )
    }
}

class DarkModeManager(initialDark: Boolean) {
    val isDark = mutableStateOf(initialDark)

    fun toggleDarkMode() {
        isDark.value = !isDark.value
    }
}

@Composable
fun DarkModeSwitch(darkModeManager: DarkModeManager) {
    val isDarkMode = darkModeManager.isDark.value
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = "Dark Mode")
        Switch(
            checked = isDarkMode,
            onCheckedChange = { darkModeManager.toggleDarkMode() }
        )
    }
}
