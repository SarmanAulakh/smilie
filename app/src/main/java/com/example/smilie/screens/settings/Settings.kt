package com.example.smilie.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    val darkModeManager = DarkModeManager()
    AppTheme(darkModeManager) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {

            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(
                    text = "SETTINGS",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                )
                DarkModeSwitch(darkModeManager)
                Button(onClick = { settingViewModel.onSignOutClick(openAndPopUp) }) {
                    Text(text = "Sign Out")
                }
                Text(
                    text = "Metrics Privacy",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                )
                MetricPrivacy(settingsList, mCheckedState)
            }
        }
    }
}

@Composable
fun MetricPrivacy(settingsList: List<SettingsItem>, mCheckedState: MutableList<Boolean>) {
    settingsList.forEachIndexed {index, item ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = item.name)
            Switch(
                checked = mCheckedState[index],
                onCheckedChange = { mCheckedState[index] = it })
        }
    }
}

class DarkModeManager {
    val isDark = mutableStateOf(false)

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
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(text = "Dark Mode")
        Switch(
            checked = isDarkMode,
            onCheckedChange = { darkModeManager.toggleDarkMode() }
        )
    }
}
