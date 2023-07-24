package com.example.smilie.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.smilie.model.Metric
import com.example.smilie.model.view.MainViewModel

@Composable
fun SettingsScreen(
    openAndPopUp: (String) -> Unit,
    metrics: ArrayList<Metric>?,
    settingsManager: SettingsManager,
    settingViewModel: SignUpViewModel = hiltViewModel()
) {
    // Declaring a boolean value for storing checked state
    val privacySettings: MutableList<MutableState<Boolean>> = remember{ mutableListOf()}

    metrics?.forEach{
        privacySettings.add(mutableStateOf(it.public))
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
            DarkModeSwitch(settingsManager)
            TextSizeSlider(settingsManager)

            Spacer(Modifier.height(30.dp))
            Text(
                text = "Metrics Privacy",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
            )
            MetricPrivacy(metrics, privacySettings)
            Button(onClick = {
                if (metrics != null) {
                    settingViewModel.saveMetrics(openAndPopUp, metrics)
                }
            }) {
                Text("Save Metric Settings")
            }
        }
    }
}

@Composable
fun MetricPrivacy(metrics: ArrayList<Metric>?, privacySettings: MutableList<MutableState<Boolean>>) {
    if(metrics != null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(metrics) { index, metric ->
                if (metric.active) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = metric.name)
                        Switch(
                            checked = privacySettings[index].value,
                            onCheckedChange = {
                                privacySettings[index].value = it
                                metrics[index].public = it
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TextSizeSlider(settingsManager: SettingsManager) {
    Text(text = "Text Size")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Slider(
            value = settingsManager.fontSizeSlider.value,
            onValueChange = { settingsManager.fontSizeSlider.value = it },
            valueRange = 0f..4f,
            steps = 3
        )
    }
}

@Composable
fun DarkModeSwitch(settingsManager: SettingsManager) {
    val isDarkMode = settingsManager.isDark.value
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
            onCheckedChange = { settingsManager.toggleDarkMode() }
        )
    }
}
