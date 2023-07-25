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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smilie.model.Metric

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
            Row(
                modifier = Modifier.fillMaxWidth().padding(15.dp),
                horizontalArrangement = Arrangement.Center, // Aligns items to the center of the row
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "SETTINGS",
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentPadding = PaddingValues(16.dp)
            ) {

                item {
                    DarkModeSwitch(settingsManager)
                }
                item {
                    TextSizeSlider(settingsManager)
                }

                item {
                    Spacer(Modifier.height(20.dp))
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(5.dp),
                        horizontalArrangement = Arrangement.Center, // Aligns items to the center of the row
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Metrics Privacy",
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                if(metrics != null) {
                    itemsIndexed(metrics) { index, _ ->
                        MetricPrivacy(metrics, index, privacySettings)
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(5.dp),
                        horizontalArrangement = Arrangement.Center, // Aligns items to the center of the row
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(onClick = {
                            if (metrics != null) {
                                settingViewModel.saveMetrics(openAndPopUp, metrics)
                            }
                        }) {
                            Text(
                                text = "Save Metric Privacy Settings",
                                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(5.dp),
                        horizontalArrangement = Arrangement.Center, // Aligns items to the center of the row
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            onClick = { settingViewModel.onSignOutClick(openAndPopUp) },
                        ) {
                            Text(
                                text = "Sign Out",
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricPrivacy(metrics: ArrayList<Metric>?, index: Int, privacySettings: MutableList<MutableState<Boolean>>) {
    if (metrics == null) {
        return
    }

    if (metrics[index].active) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = metrics[index].name)
            Switch(
                checked = !privacySettings[index].value,
                onCheckedChange = {
                    privacySettings[index].value = !it
                    metrics[index].public = !it
                }
            )
        }
    }
}

@Composable
fun TextSizeSlider(settingsManager: SettingsManager) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start, // Aligns items to the start (left) of the row
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Text Size",
            modifier = Modifier.padding(start = 10.dp)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
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
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "A",
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp)
        )
        Text(
            text = "A",
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 10.dp)
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
