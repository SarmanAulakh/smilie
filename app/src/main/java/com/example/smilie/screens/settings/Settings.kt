package com.example.smilie.screens.settings

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.completion.TextCompletion
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.smilie.ui.theme.ResizeableTypography

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

//            val openaiToken: String = ""
//            val openai = OpenAI(openaiToken)
//
//            val completionRequest = CompletionRequest(
//                model = ModelId("text-curie-001"),
//                prompt = "In list form, how can I improve my sleep?",
//                maxTokens = 250,
//                echo = true
//            )
//            var inputText = remember { mutableStateOf("") }
//            LaunchedEffect(Unit) {
//                val completion = openai.completion(completionRequest)
//                Log.d("OpenAI", completion.choices[0].text)
//                inputText.value = completion.choices[0].text
//            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(10.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween,
//            ) {
//                Text(inputText.value)
//            }
//
//            val openai2 = OpenAI(openaiToken)
//            val completionRequest2 = CompletionRequest(
//                model = ModelId("text-curie-001"),
//                prompt = "In list form, how can I improve time spent with friends?",
//                maxTokens = 250,
//                echo = true
//            )
//
//            var inputText2 = remember { mutableStateOf("") }
//            LaunchedEffect(Unit) {
//                val completion2 = openai2.completion(completionRequest2)
//                Log.d("OpenAI", completion2.choices[0].text)
//                inputText2.value = completion2.choices[0].text
//            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(10.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween,
//            ) {
//                Text(inputText2.value)
//            }

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

//suspend fun getFeedback(): TextCompletion {
//    val openai = OpenAI(openaiToken)
//
//    val completionRequest = CompletionRequest(
//        model = ModelId("text-ada-001"),
//        prompt = "Somebody once told me the world is gonna roll me",
//        echo = true
//    )
//
//    return openai.completion(completionRequest)
//}
