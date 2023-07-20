package com.example.smilie.screens.settings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.completion.TextCompletion
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI

import kotlinx.coroutines.*

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

            // ChatGPT
//            var editText = remember { mutableStateOf("") }
//            var messageList: MutableList<Message> = remember { mutableStateListOf() }
//
//            EditTextField(editText)
//            Button(onClick = {
//                if (editText.value.isNotEmpty()) {
//                    var userMessage = Message(editText.value.trim(), Message.SENT_BY_ME)
//                    messageList.add(userMessage)
//                    editText.value = ""
//
//                    var chatbotMessage = Message("Typing... ", Message.SENT_BY_BOT)
//                    messageList.add(chatbotMessage)
//
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val openaiToken = "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"
//                        val openai = OpenAI(openaiToken)
//                        val completionRequest = CompletionRequest(
//                            model = ModelId("text-curie-001"),
//                            prompt = userMessage.text,
//                            maxTokens = 250,
//                            echo = false
//                        )
//                        Log.d("ChatGPT", "${completionRequest.prompt}")
//                        val completion = openai.completion(completionRequest)
//                        Log.d("OpenAI", completion.choices[0].text)
//                        if (messageList.size > 0) {
//                            messageList.removeLast()
//                        }
//                        chatbotMessage = Message(completion.choices[0].text.trim(), Message.SENT_BY_BOT)
//                        messageList.add(chatbotMessage)
//                    }
//                    Log.d("SmilieSettings", messageList.size.toString())
//                }
//            }) {
//                Text("Send")
//            }
//
//            LazyColumn(
//                modifier = Modifier.fillMaxWidth(),
//                contentPadding = PaddingValues(16.dp)
//            ) {
//                itemsIndexed(messageList) { index, message ->
//                    MessageBubble(message)
//                }
//            }
//            Spacer(Modifier.height(30.dp))

            // ChatGPT
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

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EditTextField(editText: MutableState<String>) {
//
//    TextField(
//        value = editText.value,
//        onValueChange = { editText.value = it },
//    )
//}

//class Message(var text: String, var sentBy: String) {
//
//    companion object {
//        var SENT_BY_ME = "me"
//        var SENT_BY_BOT = "bot"
//    }
//}

//@Composable
//fun MessageBubble(message: Message) {
//    val bubbleColor = if (message.sentBy == Message.SENT_BY_ME) Color.Blue else Color.Gray
//    val textColor = if (message.sentBy == Message.SENT_BY_ME) Color.White else Color.Black
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//        horizontalArrangement = if (message.sentBy == Message.SENT_BY_ME) Arrangement.End else Arrangement.Start
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth(0.85f)
//                .padding(8.dp)
//                .background(
//                    color = bubbleColor,
//                    shape = RoundedCornerShape(8.dp)
//                )
//                .padding(8.dp),
//        ) {
//            Text(
//                text = message.text,
//                color = textColor,
//            )
//        }
//    }
//}
